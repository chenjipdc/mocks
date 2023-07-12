package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.sink.db.IgniteSinkConfig;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

@Slf4j
@AutoService(SinkPlugin.class)
public class IgniteSinkPlugin extends JdbcSinkPlugin<IgniteSinkConfig> {

    @Override
    public String type() {
        return "ignite";
    }

    @Override
    public void initConfig(String config) {
        sinkConfig = JSONObject.parseObject(config,
                IgniteSinkConfig.class);
    }
}
