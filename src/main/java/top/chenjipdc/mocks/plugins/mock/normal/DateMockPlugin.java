package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.DateMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class DateMockPlugin extends AbstractMockPlugin<Object> {

    private DateMockConfig dateConfig;

    @Override
    public String type() {
        return "date";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        dateConfig = JSONObject.parseObject(config.getConfig(),
                DateMockConfig.class);
    }

    @Override
    public Map<String, Object> value() {
        Map<String, Object> map = new HashMap<>();
        for (String column : aliases.values()) {
            if (dateConfig != null) {
                String timeZoneId = dateConfig.getTimeZoneId();
                String format = dateConfig.getFormat();
                Date start = dateConfig.getStart();
                Date end = dateConfig.getEnd();
                if (start != null && end != null) {
                    map.put(column,
                            DateUtils.randomStringRangeDate(start.getTime(),
                                    end.getTime(),
                                    format,
                                    timeZoneId));
                    break;
                }
                if (start != null) {
                    map.put(column,
                            DateUtils.randomStringRangeDate(start.getTime(),
                                    new Date().getTime(),
                                    format,
                                    timeZoneId));
                    break;
                }
                if (end != null) {
                    map.put(column,
                            DateUtils.randomString(
                                    end.getTime(),
                                    format,
                                    timeZoneId));
                    break;
                }
            }
            map.put(column,
                    DateUtils.randomString());
        }
        return map;
    }
}
