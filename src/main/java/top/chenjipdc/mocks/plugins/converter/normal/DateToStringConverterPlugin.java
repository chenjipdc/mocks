package top.chenjipdc.mocks.plugins.converter.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.converter.normal.DateConverterConfig;
import top.chenjipdc.mocks.plugins.ConverterPlugin;
import top.chenjipdc.mocks.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@AutoService(ConverterPlugin.class)
public class DateToStringConverterPlugin implements ConverterPlugin<Date, String> {

    private SimpleDateFormat format;

    @Override
    public String type() {
        return "date-to-string";
    }

    @Override
    public void init(String config) {
        DateConverterConfig converterConfig = JSONObject.parseObject(config,
                DateConverterConfig.class);

        format = DateUtils.dateFormat(converterConfig.getFormat(),
                converterConfig.getTimeZoneId());
    }

    @Override
    public String convert(Date value) {
        if (value == null) {
            return null;
        }
        return format.format(value);
    }
}
