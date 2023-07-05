package top.chenjipdc.mocks.plugins.converter;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.ConverterPlugin;
import top.chenjipdc.mocks.utils.StringUtils;

@AutoService(ConverterPlugin.class)
public class QuoteStringConverterPlugin implements ConverterPlugin<String, String> {
    @Override
    public String type() {
        return "quote-string";
    }

    @Override
    public String convert(String value) {
        return StringUtils.quote(value);
    }
}
