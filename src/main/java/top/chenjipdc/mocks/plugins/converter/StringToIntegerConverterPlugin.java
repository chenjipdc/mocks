package top.chenjipdc.mocks.plugins.converter;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringToIntegerConverterPlugin implements ConverterPlugin<String, Integer> {
    @Override
    public String type() {
        return "string-to-int";
    }

    @Override
    public Integer convert(String value) {
        return Integer.parseInt(value);
    }
}
