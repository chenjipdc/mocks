package top.chenjipdc.mocks.run;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;
import top.chenjipdc.mocks.plugins.filter.FilterPlugin;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.MockLeftCycle;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class MockService implements LifeCycle {

    private final List<Config.MocksConfig> mocksConfig;

    private Set<String> allColumn = new HashSet<>();

    /**
     * key: 数据源分组
     * value: mock插件
     */
    private Map<String, MockPlugin<Object>> dsPluginMap = new HashMap<>();

    /**
     * key: 数据源分组
     * value: 过滤器
     */
    private Map<String, List<FilterPlugin>> filters = new HashMap<>();

    /**
     * key: 数据源分组
     * value: {key:字段， values:转换器}
     */
    private Map<String, Multimap<String, ConverterPlugin<Object, Object>>> converters = new HashMap<>();

    /**
     * key: 数据源分组
     */
    private Map<String, Boolean> filterBeforeConverters = new HashMap<>();

    public MockService(List<Config.MocksConfig> mocksConfig) {
        this.mocksConfig = mocksConfig;
    }

    public void init() {
        check();
        mocksConfig
                .forEach(this::initPlugin);
    }

    private void initPlugin(Config.MocksConfig config) {
        ServiceLoader<MockPlugin> loader = ServiceLoader.load(MockPlugin.class);
        loader.forEach(mockPlugin -> {
            if (mockPlugin.type()
                    .equals("ignore")) {
                return;
            }

            if (mockPlugin.type()
                    .equals(config.getType())) {

                Collection<String> columns = getColumns(config);
                String group = String.join(",",
                        columns);
                allColumn.add(group);
                dsPluginMap.put(group,
                        mockPlugin);
                mockPlugin.init(config);

                filterBeforeConverters.put(group,
                        config.getFilterBeforeConverter());

                initConverter(group,
                        config);

                initFilters(group,
                        config);
            }
        });
    }

    private void initConverter(String group, Config.MocksConfig config) {
        if (config.getConverters() != null && config.getConverters()
                .size() > 0) {
            Multimap<String, ConverterPlugin<Object, Object>> converterPluginMap = ArrayListMultimap.create();
            ServiceLoader<ConverterPlugin> converterPlugins = ServiceLoader.load(ConverterPlugin.class);
            config.getConverters()
                    .forEach(it -> {
                        for (ConverterPlugin converterPlugin : converterPlugins) {
                            if (converterPlugin.type()
                                    .equals(it.getType())) {
                                converterPlugin.init(it.getConfig());
                                converterPluginMap.put(it.getColumn(),
                                        converterPlugin);
                                break;
                            }
                        }
                    });
            converters.put(group,
                    converterPluginMap);

        }
    }

    private void initFilters(String group, Config.MocksConfig config) {
        if (config.getFilters() != null && config.getFilters()
                .size() > 0) {
            List<FilterPlugin> filterPlugins = new ArrayList<>();
            ServiceLoader<FilterPlugin> converterPlugins = ServiceLoader.load(FilterPlugin.class);
            config.getFilters()
                    .forEach(it -> {
                        for (FilterPlugin filterPlugin : converterPlugins) {
                            if (filterPlugin.type()
                                    .equals(it.getType())) {
                                filterPlugin.init(it.getConfig());
                                filterPlugins.add(filterPlugin);
                                break;
                            }
                        }
                    });

            filters.put(group,
                    filterPlugins);
        }
    }

    public Map<String, Object> get() {
        Map<String, Object> map = new LinkedHashMap<>();
        allColumn.forEach(group -> {
            Map<String, Object> values = get(group);
            if (values != null) {
                map.putAll(values);
            }
        });
        return map;
    }


    /**
     * 获取值，内部实现了filter的逻辑
     *
     * @param group 数据源分组
     * @return value
     */
    private Map<String, Object> get(String group) {
        MockPlugin<Object> mockPlugin = dsPluginMap.get(group);
        boolean filterBeforeConverter = filterBeforeConverters.get(group);
        Map<String, Object> values;
        if (filterBeforeConverter) {
            // filter
            if (filters != null && !filters.isEmpty()) {
                // 未匹配上则继续拿下一条数据
                boolean filter;
                do {
                    values = mockPlugin.value();
                    if (values == null || values.isEmpty()) {
                        break;
                    }
                    filter = filter(values,
                            group);
                } while (!filter);

            } else {
                values = mockPlugin.value();
            }

            // convert
            values = convert(values,
                    group);
        } else {
            if (filters != null && !filters.isEmpty()) {

                // 未匹配上则继续拿下一条数据
                boolean filter;
                do {
                    values = convert(mockPlugin.value(),
                            group);
                    if (values == null || values.isEmpty()) {
                        break;
                    }
                    filter = filter(values,
                            group);
                } while (!filter);
            } else {
                values = convert(mockPlugin.value(),
                        group);
            }
        }
        return values;
    }

    /**
     * 字段转换
     *
     * @param values values
     * @param group  数据源分组
     * @return values
     */
    private Map<String, Object> convert(Map<String, Object> values, String group) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        Map<String, Object> map = new LinkedHashMap<>();

        if (converters == null || converters.isEmpty()) {
            map.putAll(values);
        } else {
            final Multimap<String, ConverterPlugin<Object, Object>> pluginMap = converters.get(group);
            values.forEach((key, value) -> {
                Object v = value;
                // convert
                if (pluginMap != null && pluginMap.size() > 0) {
                    Collection<ConverterPlugin<Object, Object>> plugins = pluginMap.get(key);
                    for (ConverterPlugin<Object, Object> plugin : plugins) {
                        v = plugin.convert(v);
                    }
                }
                map.put(key,
                        v);
            });
        }

        return map;
    }

    /**
     * 是否过滤
     *
     * @param values values
     * @param group  数据源分组
     * @return true/不过滤 false/过滤
     */
    public boolean filter(Map<String, Object> values, String group) {
        if (values == null || values.isEmpty()) {
            return false;
        }
        List<FilterPlugin> filterPlugins = filters.get(group);
        if (filterPlugins != null && filterPlugins.size() > 0) {
            // 只要有过滤器未匹配上，就过滤掉这条数据
            for (FilterPlugin filter : filterPlugins) {
                if (!filter.filter(values)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void stop() {
        dsPluginMap.values()
                .forEach(MockLeftCycle::stop);

        converters.values()
                .forEach(it -> it.values()
                        .forEach(ConverterPlugin::stop));

        filters.values()
                .forEach(it -> it.forEach(FilterPlugin::stop));

    }

    private void check() {
        mocksConfig
                .stream()
                .filter(it -> !it.getType()
                        .equals("ignore"))
                .map(this::getColumns)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(it -> it))
                .forEach((key, value) -> {
                    if (value.size() > 1) {
                        throw new RuntimeException("mocks字段重复：" + key);
                    }
                });
    }

    /**
     * 获取数据源字段名称
     *
     * @param mock 配置
     * @return aliases转换后的字段
     */
    private Collection<String> getColumns(Config.MocksConfig mock) {
        Map<String, String> aliases = new LinkedHashMap<>();
        for (String column : mock.getColumns()) {
            if (mock.getAliases() != null && mock.getAliases()
                    .containsKey(column)) {
                aliases.put(column,
                        mock.getAliases()
                                .get(column));
            } else {
                aliases.put(column,
                        column);
            }
        }
        return aliases.values();
    }
}
