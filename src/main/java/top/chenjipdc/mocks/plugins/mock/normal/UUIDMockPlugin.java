package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.UUIDMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AutoService(MockPlugin.class)
public class UUIDMockPlugin extends AbstractMockPlugin<String> {

    private UUIDMockConfig mockConfig;

    @Override
    public String type() {
        return "uuid";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);

        mockConfig = JSONObject.parseObject(config.getConfig(),
                UUIDMockConfig.class);
    }

    @Override
    public Map<String, String> value() {
        Map<String, String> map = new HashMap<>();
        for (String column : aliases.values()) {
            String uuid = UUID.randomUUID()
                    .toString();
            map.put(column,
                    mockConfig.getPretty() ? uuid.replaceAll("-",
                            "") : uuid);
        }
        return map;
    }
}
