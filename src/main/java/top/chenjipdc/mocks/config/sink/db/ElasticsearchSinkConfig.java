package top.chenjipdc.mocks.config.sink.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

@Getter
@Setter
public class ElasticsearchSinkConfig extends SinkConfig {

    /**
     * host
     */
    private String host = "localhost";

    /**
     * port
     */
    private int port = 9200;

    /**
     * 密码
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
     * id字段名称, mappings后的名称
     */
    private String id;
}
