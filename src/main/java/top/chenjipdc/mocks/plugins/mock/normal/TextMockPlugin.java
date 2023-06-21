package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.TextMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.*;

@AutoService(MockPlugin.class)
public class TextMockPlugin extends AbstractMockPlugin<Object> {

    private final List<Object> values = new ArrayList<>();

    @Override
    public String type() {
        return "text";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        TextMockConfig mockConfig = JSONObject.parseObject(config.getConfig(),
                TextMockConfig.class);
        values.addAll(Arrays.asList(mockConfig.getText()
                .split(mockConfig.getSplit())));
    }

    @Override
    public Map<String, Object> value() {
        Map<String, Object> map = new HashMap<>();
        for (String column : aliases.values()) {
            map.put(column,
                    values.get(NumericUtils.nextInt(values.size())));
        }
        return map;
    }
}
