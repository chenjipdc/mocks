package top.chenjipdc.mocks.config.sink.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

/**
 * 使用hset存储
 */
@Getter
@Setter
public class RedisSinkConfig extends SinkConfig {

    private String host = "localhost";

    private int port = 6379;

    private String user;

    private String password;

    private int timeout = 2000;

    private int database = 10;

    /**
     * 秒
     */
    private long ttl = 0;

    /**
     * 缓存key的prefix
     */
    private String prefix = "mocks";

    /**
     * 缓存key字段：规则为"prefix:key"
     */
    private String cacheColumn;

}
