package top.chenjipdc.mocks.plugins.converter;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.ConverterPlugin;
import top.chenjipdc.mocks.utils.DateUtils;

import java.util.Date;

@AutoService(ConverterPlugin.class)
public class DateToStringConverterPlugin implements ConverterPlugin<Date, String> {
    @Override
    public String type() {
        return "date-to-string";
    }

    @Override
    public String convert(Date value) {
        return DateUtils.defaultDateFormat()
                .format(value);
    }
}
