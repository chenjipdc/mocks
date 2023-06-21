package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.FloatMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.HashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class FloatMockPlugin extends AbstractMockPlugin<Float> {

    private FloatMockConfig floatConfig;

    @Override
    public String type() {
        return "float";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        floatConfig = JSONObject.parseObject(config.getConfig(),
                FloatMockConfig.class);
    }

    @Override
    public Map<String, Float> value() {
        Map<String, Float> map = new HashMap<>();
        for (String column : aliases.values()) {
            if (floatConfig != null) {
                Float start = floatConfig.getStart();
                Float end = floatConfig.getEnd();
                if (start != null && end != null) {
                    map.put(column,
                            NumericUtils.nextFloat(start,
                                    end));
                    break;
                }
                if (start != null) {
                    map.put(column,
                            NumericUtils.nextFloat(start,
                                    Float.MAX_VALUE));
                    break;
                }
                if (end != null) {
                    map.put(column,
                            NumericUtils.nextFloat(0,
                                    end));
                    break;
                }
            }
            map.put(column,
                    NumericUtils.nextFloat());
        }
        return map;
    }
}
