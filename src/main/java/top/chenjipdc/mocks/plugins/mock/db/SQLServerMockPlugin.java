package top.chenjipdc.mocks.plugins.mock.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.mock.db.SQLServerMockConfig;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;

@AutoService(MockPlugin.class)
public class SQLServerMockPlugin extends JdbcMockPlugin<SQLServerMockConfig> {

    @Override
    public String type() {
        return "sqlserver";
    }

    @Override
    public void initConfig(String config) {
        mockConfig = JSONObject.parseObject(config,
                SQLServerMockConfig.class);
    }
}