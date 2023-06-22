package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.SnowflakeMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.id.Sequence;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AutoService(MockPlugin.class)
public class SnowflakeMockPlugin extends AbstractMockPlugin<Long> {

    private SnowflakeMockConfig mockConfig;

    private Sequence sequence;

    @Override
    public String type() {
        return "snowflake";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);

        mockConfig = JSONObject.parseObject(config.getConfig(),
                SnowflakeMockConfig.class);

        if (mockConfig.getDatacenterId() != null && mockConfig.getWorkerId() != null) {
            sequence = new Sequence(mockConfig.getWorkerId(),
                    mockConfig.getDatacenterId());
        } else {
            sequence = new Sequence();
        }
    }

    @Override
    public Map<String, Long> value() {
        Map<String, Long> map = new HashMap<>();
        for (String column : aliases.values()) {
            map.put(column,
                    sequence.nextId());
        }
        return map;
    }
}
