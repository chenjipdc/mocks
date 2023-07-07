package top.chenjipdc.mocks.plugins.mock.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.mock.db.ClickhouseMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;

@AutoService(MockPlugin.class)
public class ClickhouseMockPlugin extends JdbcMockPlugin<ClickhouseMockConfig> {

    @Override
    public String type() {
        return "clickhouse";
    }

    @Override
    public void initConfig(String config) {
        mockConfig = JSONObject.parseObject(config,
                ClickhouseMockConfig.class);
    }
}