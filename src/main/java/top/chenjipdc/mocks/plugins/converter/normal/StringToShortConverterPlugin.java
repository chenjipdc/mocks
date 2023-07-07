package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringToShortConverterPlugin implements ConverterPlugin<String, Short> {
    @Override
    public String type() {
        return "string-to-short";
    }

    @Override
    public Short convert(String value) {
        if (value == null) {
            return null;
        }
        return Short.parseShort(value);
    }
}
