package top.chenjipdc.mocks.plugins.mock.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.mock.db.PostgreSQLMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.utils.StringUtils;

import java.util.*;

@AutoService(MockPlugin.class)
public class PostgreSQLMockPlugin extends JdbcMockPlugin<PostgreSQLMockConfig> {

    @Override
    public String type() {
        return "postgresql";
    }

    @Override
    public void initConfig(String config) {
        mockConfig = JSONObject.parseObject(config,
                PostgreSQLMockConfig.class);
    }

    public void initProperties(Properties props) {
        props.setProperty("user",
                mockConfig.getUsername());
        props.setProperty("password",
                mockConfig.getPassword());
        props.setProperty("stringtype",
                "unspecified");
    }

    @Override
    public String tableMap(String table) {
        return StringUtils.doubleQuote(table);
    }

}
