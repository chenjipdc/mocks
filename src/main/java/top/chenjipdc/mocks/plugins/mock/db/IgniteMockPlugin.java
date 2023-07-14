package top.chenjipdc.mocks.plugins.mock.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.mock.db.IgniteMockConfig;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;

@AutoService(MockPlugin.class)
public class IgniteMockPlugin extends JdbcMockPlugin<IgniteMockConfig> {

    @Override
    public String type() {
        return "ignite";
    }

    @Override
    public void initConfig(String config) {
        mockConfig = JSONObject.parseObject(config,
                IgniteMockConfig.class);
    }
}