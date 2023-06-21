package top.chenjipdc.mocks.config.sink.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

@Getter
@Setter
public class MysqlSinkConfig extends SinkConfig {

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
     * 表名
     */
    private String table;

    /**
     * 是否打印sql
     */
    private Boolean logSql = Boolean.FALSE;
}
