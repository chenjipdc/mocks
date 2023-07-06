package top.chenjipdc.mocks.config.cache;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisCacheConfig {

    private String host = "localhost";

    private int port = 6379;

    private String user;

    private String password;

    private int timeout = 2000;

    private int database = 10;

    /**
     * 缓存key的prefix
     */
    private String prefix = "mocks";

    /**
     * 跑完后是否清理掉缓存，默认不清理
     */
    private Boolean clean = Boolean.FALSE;

}
