package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class ToStringConverterPlugin implements ConverterPlugin<Object, String> {
    @Override
    public String type() {
        return "to-string";
    }

    @Override
    public String convert(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
