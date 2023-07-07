package top.chenjipdc.mocks.plugins.mock.other;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.mock.MockConfig;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;

import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class IgnoreMockPlugin extends AbstractMockPlugin<String, MockConfig> {

    @Override
    public String type() {
        return "ignore";
    }

    @Override
    public Map<String, String> value() {
        return new LinkedHashMap<>();
    }
}
