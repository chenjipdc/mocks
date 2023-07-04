package top.chenjipdc.mocks.run;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.MockLeftCycle;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class MockService implements LifeCycle {

    private final List<Config.MocksConfig> mocksConfig;

    private Set<String> allColumn = new HashSet<>();

    private Map<String, MockPlugin<Object>> dsPluginMap = new HashMap<>();

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
                for (String column : getColumns(config)) {
                    allColumn.add(column);
                    dsPluginMap.put(column,
                            mockPlugin);
                }
                mockPlugin.init(config);
            }
        });
    }

    public Map<String, Object> get() {
        Map<String, Object> map = new LinkedHashMap<>();
        allColumn.forEach(column -> map.putAll(dsPluginMap.get(column)
                .value()));
        return map;
    }

    public void stop() {
        dsPluginMap.values()
                .forEach(MockLeftCycle::stop);

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
