package top.chenjipdc.mocks.plugins.converter.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.converter.normal.DateConverterConfig;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;
import top.chenjipdc.mocks.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@AutoService(ConverterPlugin.class)
public class StringToDateConverterPlugin implements ConverterPlugin<String, Date> {

    private SimpleDateFormat format;

    @Override
    public String type() {
        return "string-to-date";
    }

    @Override
    public void init(String config) {
        DateConverterConfig converterConfig = JSONObject.parseObject(config,
                DateConverterConfig.class);

        format = DateUtils.dateFormat(converterConfig.getFormat(),
                converterConfig.getTimeZoneId());
    }

    @SneakyThrows
    @Override
    public Date convert(String value) {
        if (value == null) {
            return null;
        }
        return format.parse(value);
    }
}
