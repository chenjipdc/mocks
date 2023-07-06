package top.chenjipdc.mocks.plugins.cache.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import top.chenjipdc.mocks.config.cache.RedisCacheConfig;
import top.chenjipdc.mocks.plugins.cache.CachePlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AutoService(CachePlugin.class)
public class RedisCachePlugin implements CachePlugin<String> {

    private Jedis jedis;

    private JedisPool jedisPool;

    private RedisCacheConfig cacheConfig;

    private int size = 0;

    private final AtomicInteger offset = new AtomicInteger(0);

    @Override
    public String type() {
        return "redis";
    }

    @Override
    public void init(String config) {
        cacheConfig = JSONObject.parseObject(config,
                RedisCacheConfig.class);

        initRedis();
    }

    private void initRedis() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig,
                cacheConfig.getHost(),
                cacheConfig.getPort(),
                cacheConfig.getTimeout(),
                cacheConfig.getUser(),
                cacheConfig.getPassword(),
                cacheConfig.getDatabase());

        jedis = jedisPool.getResource();
    }

    @Override
    public void cache(Map<String, String> value) {
        String redisKey = getRedisKey(size);
        size++;
        jedis.hset(redisKey,
                value);
    }

    @Override
    public void caches(Collection<Map<String, String>> value) {
        value.forEach(this::cache);
    }

    @Override
    public Map<String, String> get(boolean random) {
        int index;
        if (random) {
            index = NumericUtils.nextInt(size);
        } else {
            index = offset.getAndIncrement();
        }

        String redisKey = getRedisKey(index);
        return jedis.hgetAll(redisKey);
    }

    private String getRedisKey(int index) {
        return cacheConfig.getPrefix() + ":" + index;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void stop() {
        if (cacheConfig.getClean()) {
            for (int i = 0; i < size; i++) {
                jedis.del(getRedisKey(i));
            }
        }
        jedisPool.close();
    }
}
