package top.chenjipdc.mocks.plugins.sink.mq;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.util.Map;

@AutoService(SinkPlugin.class)
public class KafkaSinkPlugin extends AbstractSinkPlugin {

    @Override
    public String type() {
        return "kafka";
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
        return "kafka";
    }
}
