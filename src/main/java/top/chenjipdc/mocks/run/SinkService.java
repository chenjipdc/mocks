package top.chenjipdc.mocks.run;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

import java.util.Map;

@Getter
@Setter
public class SinkService implements LifeCycle {

    private final Config.SinksConfig sinkConfig;

    private final SinkPlugin sinkPlugin;

    public SinkService(Config.SinksConfig sinkConfig, SinkPlugin sinkPlugin) {
        this.sinkConfig = sinkConfig;
        this.sinkPlugin = sinkPlugin;
    }

    public void init() {
        sinkPlugin.init(sinkConfig);
    }

    public void sink(Map<String, Object> values) {
        sinkPlugin.sink(values);
    }

    public void stop() {
        sinkPlugin.stop();
    }

}
