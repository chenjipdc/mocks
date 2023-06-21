package top.chenjipdc.mocks.config.sink.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;


@Getter
@Setter
public class MongoSinkConfig extends SinkConfig {

    /**
     * address
     */
    private String address = "localhost:27017";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * database
     */
    private String database;

    /**
     * collection
     */
    private String collection;

    /**
     * uri连接参数
     */
    private String connectParams;
}
