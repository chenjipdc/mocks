package top.chenjipdc.mocks.plugins.sink.other;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.sink.SinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.util.Map;

@Slf4j
@AutoService(SinkPlugin.class)
public class IgnoreSinkPlugin extends AbstractSinkPlugin<SinkConfig> {

    @Override
    public String type() {
        return "ignore";
    }

    @Override
    public void sink(Map<String, Object> values) {

    }
}
