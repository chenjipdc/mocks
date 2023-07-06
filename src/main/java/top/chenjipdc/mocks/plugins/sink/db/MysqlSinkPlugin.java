package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.sink.db.MysqlSinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;

@Slf4j
@AutoService(SinkPlugin.class)
public class MysqlSinkPlugin extends JdbcSinkPlugin<MysqlSinkConfig> {

    @Override
    public String type() {
        return "mysql";
    }

    @Override
    public void initConfig(String config) {
        sinkConfig = JSONObject.parseObject(config,
                MysqlSinkConfig.class);
    }
}
