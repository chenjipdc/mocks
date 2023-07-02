package top.chenjipdc.mocks.plugins.mock.other;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.MockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;

import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class ThreadMockPlugin extends AbstractMockPlugin<String, MockConfig> {

    @Override
    public String type() {
        return "thread";
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
                    Thread.currentThread()
                            .getName());
        }
        return map;
    }
}
