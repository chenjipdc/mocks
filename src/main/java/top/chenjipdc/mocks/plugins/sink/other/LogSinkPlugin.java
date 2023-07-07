package top.chenjipdc.mocks.plugins.sink.other;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.other.LogSinkConfig;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.util.Map;

@Slf4j
@AutoService(SinkPlugin.class)
public class LogSinkPlugin extends AbstractSinkPlugin<LogSinkConfig> {

    private LogSinkConfig logConfig;

    @Override
    public String type() {
        return "log";
    }

    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        this.logConfig = JSONObject.parseObject(config.getConfig(),
                LogSinkConfig.class);
    }

    @Override
    public void sink(Map<String, Object> values) {
        String str;
        if (logConfig.getPretty()) {
            str = JSONObject.toJSONString(mappingsConvert(values));
        } else {
            str = mappingsConvert(values).toString();
        }
        log.info(str);
    }

    @Override
    public void stop() {
        super.stop();
    }
}
