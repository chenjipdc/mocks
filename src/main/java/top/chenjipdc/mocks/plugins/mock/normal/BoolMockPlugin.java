package top.chenjipdc.mocks.plugins.mock.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.MockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class BoolMockPlugin extends AbstractMockPlugin<Boolean, MockConfig> {


    @Override
    public String type() {
        return "bool";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
    }

    @Override
    public Map<String, Boolean> value() {
        Map<String, Boolean> map = new LinkedHashMap<>();
        for (String column : aliases.values()) {
            map.put(column,
                    NumericUtils.nextBool());
        }
        return map;
    }
}
