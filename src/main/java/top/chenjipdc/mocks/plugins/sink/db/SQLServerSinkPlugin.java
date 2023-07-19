package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.sink.db.SQLServerSinkConfig;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

@Slf4j
@AutoService(SinkPlugin.class)
public class SQLServerSinkPlugin extends JdbcSinkPlugin<SQLServerSinkConfig> {

    @Override
    public String type() {
        return "sqlserver";
    }

    @Override
    public void initConfig(String config) {
        sinkConfig = JSONObject.parseObject(config,
                SQLServerSinkConfig.class);
    }
}
