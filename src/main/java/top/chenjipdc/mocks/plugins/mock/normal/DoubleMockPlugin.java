package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.DoubleMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.HashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class DoubleMockPlugin extends AbstractMockPlugin<Double> {
    private DoubleMockConfig doubleConfig;

    @Override
    public String type() {
        return "double";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        doubleConfig = JSONObject.parseObject(config.getConfig(),
                DoubleMockConfig.class);
    }

    @Override
    public Map<String, Double> value() {
        Map<String, Double> map = new HashMap<>();
        for (String column : aliases.values()) {
            if (doubleConfig != null) {
                Double start = doubleConfig.getStart();
                Double end = doubleConfig.getEnd();
                if (start != null && end != null) {
                    map.put(column,
                            NumericUtils.nextDouble(start,
                                    end));
                    break;
                }
                if (start != null) {
                    map.put(column,
                            NumericUtils.nextDouble(start,
                                    Double.MAX_VALUE));
                    break;
                }
                if (end != null) {
                    map.put(column,
                            NumericUtils.nextDouble(0,
                                    end));
                    break;
                }
            }
            map.put(column,
                    NumericUtils.nextDouble());
        }
        return map;
    }
}
