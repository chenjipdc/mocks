package top.chenjipdc.mocks.config.mock.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class MysqlMockConfig extends MockConfig {

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
     * 来源表
     */
    private String table;

    /**
     * 查询sql最后拼接字符串：例如加 'limit'
     */
    private String lastSql = "";
}
