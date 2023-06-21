package top.chenjipdc.mocks.config.mock.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class MongoMockConfig extends MockConfig {

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
     * find json
     */
    private String findJson;

    /**
     * 批量获取数据条数
     */
    private Integer batch;

    /**
     * limit size
     */
    private Integer limit;

    /**
     * uri连接参数
     */
    private String connectParams;
}
