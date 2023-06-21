package top.chenjipdc.mocks.plugins.sink.db;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.util.Map;

@AutoService(SinkPlugin.class)
public class ClickhouseSinkPlugin extends AbstractSinkPlugin {

    @Override
    public String type() {
        return "clickhouse";
    }

    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);
    }

    @Override
    public void sink(Map<String, Object> values) {

    }

    @Override
    public String logPrefix() {
        return "clickhouse";
    }
}
