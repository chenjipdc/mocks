package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.sink.db.PostgreSQLSinkConfig;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

import java.util.Properties;

@Slf4j
@AutoService(SinkPlugin.class)
public class PostgreSQLSinkPlugin extends JdbcSinkPlugin<PostgreSQLSinkConfig> {

    @Override
    public String type() {
        return "postgresql";
    }

    @Override
    public void initConfig(String config) {
        sinkConfig = JSONObject.parseObject(config,
                PostgreSQLSinkConfig.class);
    }

    @Override
    public void initProperties(Properties props) {
        super.initProperties(props);
        props.setProperty("stringtype",
                "unspecified");
    }
}
