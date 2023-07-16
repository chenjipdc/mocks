package top.chenjipdc.mocks.config.mock.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

/**
 * 使用hset存储
 */
@Getter
@Setter
public class RedisMockConfig extends MockConfig {

    private String host = "localhost";

    private int port = 6379;

    private String user;

    private String password;

    private int timeout = 2000;

    private int database = 10;

    /**
     * 缓存key的prefix
     */
    private String keyPrefix = "mocks:*";

    /**
     * 批量获取
     */
    private Integer batch = 1000;

    /**
     * 限制多少条
     */
    private Integer limit;

}
