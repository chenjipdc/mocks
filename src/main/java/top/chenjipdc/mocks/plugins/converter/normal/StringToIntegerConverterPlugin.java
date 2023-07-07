package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringToIntegerConverterPlugin implements ConverterPlugin<String, Integer> {
    @Override
    public String type() {
        return "string-to-int";
    }

    @Override
    public Integer convert(String value) {
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value);
    }
}
