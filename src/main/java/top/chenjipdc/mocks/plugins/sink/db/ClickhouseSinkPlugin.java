package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.sink.db.ClickhouseSinkConfig;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

@Slf4j
@AutoService(SinkPlugin.class)
public class ClickhouseSinkPlugin extends JdbcSinkPlugin<ClickhouseSinkConfig> {

    @Override
    public String type() {
        return "clickhouse";
    }

    @Override
    public void initConfig(String config) {

        sinkConfig = JSONObject.parseObject(config,
                ClickhouseSinkConfig.class);
    }
}
