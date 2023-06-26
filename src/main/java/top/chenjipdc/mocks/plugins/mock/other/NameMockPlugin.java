package top.chenjipdc.mocks.plugins.mock.other;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NameUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class NameMockPlugin extends AbstractMockPlugin<String> {

    @Override
    public String type() {
        return "name";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);

    }

    @Override
    public Map<String, String> value() {
        Map<String, String> map = new LinkedHashMap<>();
        for (String column : aliases.values()) {
            map.put(column,
                    NameUtils.random());
        }
        return map;
    }
}
