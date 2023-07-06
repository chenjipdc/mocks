package top.chenjipdc.mocks.config.sink.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

@Getter
@Setter
public class JdbcSinkConfig extends SinkConfig {

    /**
     * 驱动类名
     */
    private String driverClass;

    /**
     * jdbc url
     */
    private String jdbcUrl;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 初始化时执行的sql，可用于表未创建时自动创建表
     */
    private String initSql;

    /**
     * 表名
     */
    private String table;

    /**
     * 打印查询sql
     */
    private Boolean logSql = Boolean.TRUE;

}
