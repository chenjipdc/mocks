package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringToBoolConverterPlugin implements ConverterPlugin<String, Boolean> {
    @Override
    public String type() {
        return "string-to-bool";
    }

    @Override
    public Boolean convert(String value) {
        if (value == null) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }
}
