package top.chenjipdc.mocks.plugins.mock.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.db.RedisMockConfig;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AutoService(MockPlugin.class)
public class RedisMockPlugin extends AbstractMockPlugin<Object, RedisMockConfig> {

    @Override
    public String type() {
        return "redis";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);

        mockConfig = JSONObject.parseObject(config.getConfig(),
                RedisMockConfig.class);


        JedisPoolConfig poolConfig = new JedisPoolConfig();
        try (JedisPool jedisPool = new JedisPool(poolConfig,
                mockConfig.getHost(),
                mockConfig.getPort(),
                mockConfig.getTimeout(),
                mockConfig.getUser(),
                mockConfig.getPassword(),
                mockConfig.getDatabase())) {

            try (Jedis jedis = jedisPool.getResource()) {
                String key = mockConfig.getKeyPrefix();
                ScanParams scanParams = new ScanParams();
                scanParams.match(key);
                scanParams.count(mockConfig.getBatch());

                // 游标初始值为0
                String cursor = ScanParams.SCAN_POINTER_START;
                while (true) {

                    ScanResult<String> scan = jedis.scan(cursor,
                            scanParams);
                    cursor = scan.getCursor();
                    List<String> redisKeys = scan.getResult();
                    // 没有数据了
                    if (redisKeys.size() == 0) {
                        break;
                    }

                    for (String redisKey : redisKeys) {

                        Map<String, String> objMap = jedis.hgetAll(redisKey);

                        Map<String, Object> map = new LinkedHashMap<>();
                        for (String column : columns) {
                            Object object = objMap.get(column);
                            map.put(aliases.get(column),
                                    object);
                        }
                        cachePlugin.cache(map);

                        // 数据够了
                        if (mockConfig.getLimit() != null && mockConfig.getLimit() <= cachePlugin.size()) {
                            break;
                        }
                    }

                    // 没有数据了
                    if (ScanParams.SCAN_POINTER_START.equals(cursor)) {
                        break;
                    }
                }
            }
        }
    }
}