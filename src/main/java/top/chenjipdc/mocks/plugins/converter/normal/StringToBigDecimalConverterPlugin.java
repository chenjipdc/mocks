package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.ConverterPlugin;

import java.math.BigDecimal;

@AutoService(ConverterPlugin.class)
public class StringToBigDecimalConverterPlugin implements ConverterPlugin<String, BigDecimal> {
    @Override
    public String type() {
        return "string-to-decimal";
    }

    @Override
    public BigDecimal convert(String value) {
        if (value == null) {
            return null;
        }
        return new BigDecimal(value);
    }
}
