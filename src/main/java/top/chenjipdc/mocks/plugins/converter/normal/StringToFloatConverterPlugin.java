package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringToFloatConverterPlugin implements ConverterPlugin<String, Float> {
    @Override
    public String type() {
        return "string-to-float";
    }

    @Override
    public Float convert(String value) {
        if (value == null) {
            return null;
        }
        return Float.parseFloat(value);
    }
}
