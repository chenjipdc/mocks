package top.chenjipdc.mocks.plugins.mock.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.GeoUtils;

import java.util.HashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class GeoMockPlugin extends AbstractMockPlugin<GeoUtils.GeoPoint> {
    @Override
    public String type() {
        return "geo";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);

    }

    @Override
    public Map<String, GeoUtils.GeoPoint> value() {
        final Map<String, GeoUtils.GeoPoint> map = new HashMap<>();
        for (String column : aliases.values()) {
            map.put(column,
                    GeoUtils.random());
        }
        return map;
    }
}
