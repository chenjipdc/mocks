package top.chenjipdc.mocks.config.sink.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

@Getter
@Setter
public class ClickhourceSinkConfig extends SinkConfig {

    /**
     * jdbc url
     */
    private String jdbcUrl = "jdbc:ch://localhost:8123/sink";

    /**
     * 用户名
     */
    private String username = "default";

    /**
     * 密码
     */
    private String password = "";

    /**
     * 表名
     */
    private String table;

    /**
     * 是否打印sql
     */
    private Boolean logSql = Boolean.FALSE;
}
