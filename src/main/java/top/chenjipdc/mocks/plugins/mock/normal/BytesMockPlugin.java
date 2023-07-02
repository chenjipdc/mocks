package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.BytesMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class BytesMockPlugin extends AbstractMockPlugin<Byte[], BytesMockConfig> {

    @Override
    public String type() {
        return "bytes";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                BytesMockConfig.class);
    }

    @Override
    public Map<String, Byte[]> value() {
        Map<String, Byte[]> map = new LinkedHashMap<>();
        int length = mockConfig.getLength();

        for (String column : aliases.values()) {
            Byte[] bytes = new Byte[length];
            byte[] nextedBytes = NumericUtils.nextBytes(length);
            for (int i = 0; i < length; i++) {
                bytes[i] = nextedBytes[i];
            }
            map.put(column,
                    bytes);
        }
        return map;
    }
}
