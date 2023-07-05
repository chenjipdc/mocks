package top.chenjipdc.mocks.plugins.converter;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class LongToStringConverterPlugin implements ConverterPlugin<Long, String> {
    @Override
    public String type() {
        return "long-to-string";
    }

    @Override
    public String convert(Long value) {
        return value.toString();
    }
}
