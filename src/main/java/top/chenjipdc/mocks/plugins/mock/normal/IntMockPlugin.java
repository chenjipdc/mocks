package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.IntMockConfig;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AutoService(MockPlugin.class)
public class IntMockPlugin extends AbstractMockPlugin<Integer, IntMockConfig> {

    private AtomicInteger value = new AtomicInteger(1);

    @Override
    public String type() {
        return "int";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                IntMockConfig.class);

        checkAndInit();
    }

    @Override
    public Map<String, Integer> value() {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (String column : aliases.values()) {
            if (mockConfig != null) {
                // 自增
                if (mockConfig.getAutoincrement()) {
                    int value = this.value.getAndIncrement();
                    if (value == Integer.MAX_VALUE) {
                        throw new RuntimeException("自增已超出int数据范围, 请调整生成的数据总量");
                    }
                    map.put(column,
                            value);
                    break;
                }

                Integer start = mockConfig.getStart();
                Integer end = mockConfig.getEnd();
                if (start != null && end != null) {
                    map.put(column,
                            NumericUtils.nextInt(start,
                                    end));
                    break;
                }
                if (start != null) {
                    map.put(column,
                            NumericUtils.nextInt(start,
                                    Integer.MAX_VALUE));
                    break;
                }
                if (end != null) {
                    map.put(column,
                            NumericUtils.nextInt(0,
                                    end));
                    break;
                }
            }
            map.put(column,
                    NumericUtils.nextInt());
        }
        return map;
    }

    private void checkAndInit() {
        if (mockConfig.getAutoincrement()) {
            if (mockConfig.getStart() != null) {
                value = new AtomicInteger(mockConfig.getStart());
            }
        }
    }
}
