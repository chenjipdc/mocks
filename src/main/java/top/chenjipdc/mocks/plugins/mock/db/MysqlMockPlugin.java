package top.chenjipdc.mocks.plugins.mock.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.mock.db.MysqlMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;

import java.util.*;

@AutoService(MockPlugin.class)
public class MysqlMockPlugin extends JdbcMockPlugin<MysqlMockConfig> {

    @Override
    public String type() {
        return "mysql";
    }

    @Override
    public void initConfig(String config) {
        mockConfig = JSONObject.parseObject(config,
                MysqlMockConfig.class);
    }
}