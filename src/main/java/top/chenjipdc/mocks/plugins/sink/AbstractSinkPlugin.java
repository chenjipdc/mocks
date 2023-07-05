package top.chenjipdc.mocks.plugins.sink;

import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.SinkConfig;
import top.chenjipdc.mocks.plugins.ConverterPlugin;
import top.chenjipdc.mocks.plugins.SinkPlugin;

import java.util.*;



@Slf4j
public abstract class AbstractSinkPlugin<T extends SinkConfig> implements SinkPlugin {

    protected Config.SinksConfig config;

    protected T sinkConfig;

    /**
     * 映射字段转换器
     */
    protected Map<String, ConverterPlugin<Object, Object>> converters = new IdentityHashMap<>();

    private long start = System.currentTimeMillis();


    public void init(Config.SinksConfig config) {
        this.config = config;
        startTiming();

        initConverter();
    }

    private void initConverter() {
        // 转换器配置
        if (config.getConverters() != null && config.getConverters()
                .size() > 0) {
            ServiceLoader<ConverterPlugin> converterPlugins = ServiceLoader.load(ConverterPlugin.class);
            config.getConverters()
                    .forEach(it -> {
                        for (ConverterPlugin converterPlugin : converterPlugins) {
                            if (converterPlugin.type()
                                    .equals(it.getType())) {
                                converterPlugin.init(it.getConfig());
                                converters.put(it.getColumn(),
                                        converterPlugin);
                                break;
                            }
                        }
                    });
        }
    }

    /**
     * 将数据转换成mappings里的数据，主要转换key
     *
     * @param values 数据池的一条数据
     * @return 转换后的数据
     */
    public Map<String, Object> mappingsConvert(Map<String, Object> values) {
        Map<String, Object> map;
        Map<String, String> mappings = config.getMappings();
        if (mappings != null) {
            map = new LinkedHashMap<>();
            mappings.forEach((k, v) -> map.put(k,
                    values.get(v)));
        } else {
            map = values;
        }

        // 转换值
        if (converters != null) {
            converters.forEach((column, converter) -> {
                if (map.containsKey(column)) {
                    map.put(column, converter.convert(map.get(column)));
                }
            });
        }
        return map;
    }

    public void stop() {
        stopTiming();

        converters.values().forEach(ConverterPlugin::stop);
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
