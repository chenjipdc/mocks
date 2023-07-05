package top.chenjipdc.mocks.plugins.converter;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class IntegerToStringConverterPlugin implements ConverterPlugin<Integer, String> {
    @Override
    public String type() {
        return "int-to-string";
    }

    @Override
    public String convert(Integer value) {
        return value.toString();
    }
}
