package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringToLongConverterPlugin implements ConverterPlugin<String, Long> {
    @Override
    public String type() {
        return "string-to-long";
    }

    @Override
    public Long convert(String value) {
        if (value == null) {
            return null;
        }
        return Long.parseLong(value);
    }
}
