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
    private String jdbcUrl = "jdbc:mysql://localhost:3306/sink?characterEncoding=utf8";

    /**
     * 用户名
     */
    private String username = "root";

    /**
     * 密码
     */
    private String password = "root";

    /**
     * 表名
     */
    private String table;

    /**
     * 是否打印sql
     */
    private Boolean logSql = Boolean.FALSE;
}
