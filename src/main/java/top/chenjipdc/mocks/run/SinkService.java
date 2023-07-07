package top.chenjipdc.mocks.run;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.filter.FilterPlugin;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

@Getter
@Setter
public class SinkService implements LifeCycle {

    private final Config.SinksConfig sinkConfig;

    private final SinkPlugin sinkPlugin;

    /**
     * 过滤器
     */
    private List<FilterPlugin> filters = new ArrayList<>();

    public SinkService(Config.SinksConfig sinkConfig, SinkPlugin sinkPlugin) {
        this.sinkConfig = sinkConfig;
        this.sinkPlugin = sinkPlugin;
    }

    public void init() {
        sinkPlugin.init(sinkConfig);

        initFilter();
    }

    private void initFilter() {
        if (sinkConfig.getFilters() != null && sinkConfig.getFilters()
                .size() > 0) {
            ServiceLoader<FilterPlugin> converterPlugins = ServiceLoader.load(FilterPlugin.class);
            sinkConfig.getFilters()
                    .forEach(it -> {
                        for (FilterPlugin filterPlugin : converterPlugins) {
                            if (filterPlugin.type()
                                    .equals(it.getType())) {
                                filterPlugin.init(it.getConfig());
                                filters.add(filterPlugin);
                                break;
                            }
                        }
                    });
        }
    }

    public boolean filter(Map<String, Object> values) {
        if (filters != null && filters.size() > 0) {
            // 只要有过滤器未匹配上，就过滤掉这条数据
            for (FilterPlugin filter : filters) {
                if (!filter.filter(values)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void sink(Map<String, Object> values) {
        // 空
        if (values == null || values.size() == 0) {
            return;
        }
        // 过滤掉
        if (!filter(values)) {
            return;
        }

        sinkPlugin.sink(values);
    }

    public void stop() {
        sinkPlugin.stop();
    }

}
