package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.LongMockConfig;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@AutoService(MockPlugin.class)
public class LongMockPlugin extends AbstractMockPlugin<Long, LongMockConfig> {

    private AtomicLong value = new AtomicLong(1);

    @Override
    public String type() {
        return "long";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                LongMockConfig.class);

        checkAndInit();
    }

    @Override
    public Map<String, Long> value() {
        Map<String, Long> map = new LinkedHashMap<>();
        for (String column : aliases.values()) {
            if (mockConfig != null) {
                // 自增
                if (mockConfig.getAutoincrement()) {
                    long value = this.value.getAndIncrement();
                    if (value == Long.MAX_VALUE) {
                        throw new RuntimeException("自增已超出long数据范围, 请调整生成的数据总量");
                    }
                    map.put(column,
                            value);
                    break;
                }

                Long start = mockConfig.getStart();
                Long end = mockConfig.getEnd();
                if (start != null && end != null) {
                    map.put(column,
                            NumericUtils.nextLong(start,
                                    end));
                    break;
                }
                if (start != null) {
                    map.put(column,
                            NumericUtils.nextLong(start,
                                    Long.MAX_VALUE));
                    break;
                }
                if (end != null) {
                    map.put(column,
                            NumericUtils.nextLong(0,
                                    end));
                    break;
                }
            }
            map.put(column,
                    NumericUtils.nextLong());
        }
        return map;
    }

    private void checkAndInit() {
        if (mockConfig.getAutoincrement()) {
            if (mockConfig.getStart() != null) {
                value = new AtomicLong(mockConfig.getStart());
            }
        }
    }
}
