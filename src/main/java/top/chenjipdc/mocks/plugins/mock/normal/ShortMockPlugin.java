package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.ShortMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AutoService(MockPlugin.class)
public class ShortMockPlugin extends AbstractMockPlugin<Short, ShortMockConfig> {

    private AtomicInteger value = new AtomicInteger(1);


    @Override
    public String type() {
        return "short";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                ShortMockConfig.class);

        checkAndInit();
    }

    @Override
    public Map<String, Short> value() {
        Map<String, Short> map = new LinkedHashMap<>();
        for (String column : aliases.values()) {
            if (mockConfig != null) {
                // 自增
                if (mockConfig.getAutoincrement()) {
                    short value = (short) this.value.getAndIncrement();
                    if (value == Short.MAX_VALUE) {
                        throw new RuntimeException("自增已超出short数据范围, 请调整生成的数据总量");
                    }
                    map.put(column,
                            value);
                    break;
                }

                // 范围
                Short start = mockConfig.getStart();
                Short end = mockConfig.getEnd();
                if (start != null && end != null) {
                    map.put(column,
                            NumericUtils.nextShort(start,
                                    end));
                    break;
                }
                if (start != null) {
                    map.put(column,
                            NumericUtils.nextShort(start,
                                    Short.MAX_VALUE));
                    break;
                }
                if (end != null) {
                    map.put(column,
                            NumericUtils.nextShort((short) 0,
                                    end));
                    break;
                }
            }

            map.put(column,
                    NumericUtils.nextShort());
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
