package top.chenjipdc.mocks.plugins.mock;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.MockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.cache.CachePlugin;

import java.util.*;

@Getter
@Setter
public abstract class AbstractMockPlugin<V, C extends MockConfig> implements MockPlugin<V> {

    /**
     * 字段名称
     */
    protected List<String> columns;

    /**
     * 字段别名，主要用于数据池字段名字冲突
     */
    protected Map<String, String> aliases;

    /**
     * config
     */
    protected Config.MocksConfig config;

    /**
     * mock配置
     */
    protected C mockConfig;

    /**
     * 缓存插件
     */
    protected CachePlugin<V> cachePlugin;


    @Override
    public void init(Config.MocksConfig config) {
        this.config = config;
        this.columns = config.getColumns();
        this.aliases = config.getAliases();

        if (aliases == null) {
            aliases = new LinkedHashMap<>();
        }
        for (String column : config.getColumns()) {
            if (!aliases.containsKey(column)) {
                aliases.put(column,
                        column);
            }
        }

        initCaching();
    }

    private void initCaching() {
        Config.CacheConfig cacheConfig = config.getCaching();
        ServiceLoader<CachePlugin> cachePlugins = ServiceLoader.load(CachePlugin.class);
        for (CachePlugin plugin : cachePlugins) {
            if (cacheConfig != null) {
                if (plugin.type()
                        .equals(cacheConfig.getType())) {
                    cachePlugin = plugin;

                    plugin.init(cacheConfig.getConfig());
                    break;
                }
            } else {
                if (plugin.type()
                        .equals("memory")) {
                    cachePlugin = plugin;
                    break;
                }
            }
        }
    }

    @Override
    public Map<String, V> value() {
        return cachePlugin.get(mockConfig.getRandom());
    }

    @Override
    public void stop() {
        if (cachePlugin != null) {
            cachePlugin.stop();
        }
    }
}
