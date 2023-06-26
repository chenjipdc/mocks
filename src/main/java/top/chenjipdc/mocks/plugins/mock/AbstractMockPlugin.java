package top.chenjipdc.mocks.plugins.mock;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.MockPlugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractMockPlugin<T> implements MockPlugin<T> {

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
    }
}
