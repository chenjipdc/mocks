package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringToDoubleConverterPlugin implements ConverterPlugin<String, Double> {
    @Override
    public String type() {
        return "string-to-double";
    }

    @Override
    public Double convert(String value) {
        if (value == null) {
            return null;
        }
        return Double.parseDouble(value);
    }
}
