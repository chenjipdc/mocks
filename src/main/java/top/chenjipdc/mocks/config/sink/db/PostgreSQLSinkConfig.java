package top.chenjipdc.mocks.config.sink.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

import java.util.List;

@Getter
@Setter
public class PostgreSQLSinkConfig extends SinkConfig {

    /**
     * jdbc url
     */
    private String jdbcUrl = "jdbc:postgresql://localhost:5432/sink";

    /**
     * 用户名
     */
    private String username = "postgres";

    /**
     * 密码
     */
    private String password = "postgres";

    /**
     * 表名
     */
    private String table;

    /**
     * 是否打印sql
     */
    private Boolean logSql = Boolean.FALSE;
}
