package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.ShortMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AutoService(MockPlugin.class)
public class ShortMockPlugin extends AbstractMockPlugin<Short> {

    private ShortMockConfig shortConfig;

    private AtomicInteger value = new AtomicInteger(1);


    @Override
    public String type() {
        return "short";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        shortConfig = JSONObject.parseObject(config.getConfig(),
                ShortMockConfig.class);

        checkAndInit();
    }

    @Override
    public Map<String, Short> value() {
        Map<String, Short> map = new LinkedHashMap<>();
        for (String column : aliases.values()) {
            if (shortConfig != null) {
                // 自增
                if (shortConfig.getAutoincrement()) {
                    short value = (short) this.value.getAndIncrement();
                    if (value == Short.MAX_VALUE) {
                        throw new RuntimeException("自增已超出short数据范围, 请调整生成的数据总量");
                    }
                    map.put(column,
                            value);
                    break;
                }

                // 范围
                Short start = shortConfig.getStart();
                Short end = shortConfig.getEnd();
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
        if (shortConfig.getAutoincrement()) {
            if (shortConfig.getStart() != null) {
                value = new AtomicInteger(shortConfig.getStart());
            }
        }
    }
}
