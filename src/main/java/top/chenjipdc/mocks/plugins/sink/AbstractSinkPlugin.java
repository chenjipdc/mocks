package top.chenjipdc.mocks.plugins.sink;

import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.SinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;


@Slf4j
public abstract class AbstractSinkPlugin<T extends SinkConfig> implements SinkPlugin {

    protected Config.SinksConfig config;

    protected T sinkConfig;

    private long start = System.currentTimeMillis();


    public void init(Config.SinksConfig config) {
        this.config = config;
        startTiming();
    }

    /**
     * 将数据转换成mappings里的数据，主要转换key
     *
     * @param values 数据池的一条数据
     * @return 转换后的数据
     */
    public Map<String, Object> mappingsConvert(Map<String, Object> values) {
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, String> mappings = config.getMappings();
        if (mappings != null) {
            mappings.forEach((k, v) -> map.put(k,
                    values.get(v)));
            return map;
        } else {
            return values;
        }
    }

    public void stop() {
        stopTiming();
    }

    public void startTiming() {
        start = System.currentTimeMillis();
    }

    public void stopTiming() {
        log.info("sink=>" + config.getName() + logPrefix() + "耗时(秒): {}",
                (System.currentTimeMillis() - start) / 1000.00);
    }

    public String logPrefix() {
        return "处理";
    }
}
