package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.db.RedisSinkConfig;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@AutoService(SinkPlugin.class)
public class RedisSinkPlugin extends AbstractSinkPlugin<RedisSinkConfig> {

    private Jedis jedis;

    private JedisPool jedisPool;

    private RedisSinkConfig sinkConfig;

    private List<Map<String, Object>> cacheValues = new ArrayList<>();

    @Override
    public String type() {
        return "redis";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                RedisSinkConfig.class);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig,
                sinkConfig.getHost(),
                sinkConfig.getPort(),
                sinkConfig.getTimeout(),
                sinkConfig.getUser(),
                sinkConfig.getPassword(),
                sinkConfig.getDatabase());

        jedis = jedisPool.getResource();
    }

    @Override
    public void sink(Map<String, Object> values) {
        if (sinkConfig.getBatch() > 1) {
            cacheValues.add(values);
            if (cacheValues.size() >= sinkConfig.getBatch()) {
                saveBatch();
            }
        } else {
            saveOne(values);
        }
    }

    private void saveBatch() {
        if (cacheValues == null || cacheValues.size() == 0) {
            return;
        }
        for (Map<String, Object> cacheValue : cacheValues) {
            saveOne(cacheValue);
        }
        cacheValues = new ArrayList<>();
    }

    private void saveOne(Map<String, Object> values) {
        Map map = mappingsConvert(values);
        String hKey = sinkConfig.getPrefix() + ":" + map.get(sinkConfig.getCacheColumn());
        jedis.hset(hKey,
                map);
        if (sinkConfig.getTtl() > 0) {
            jedis.expire(hKey, sinkConfig.getTtl());
        }
    }

    @SneakyThrows
    @Override
    public void stop() {
        super.stop();

        saveBatch();

        jedisPool.close();
    }
}
