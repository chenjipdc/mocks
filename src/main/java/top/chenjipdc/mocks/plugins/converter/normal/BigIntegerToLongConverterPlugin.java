package top.chenjipdc.mocks.plugins.converter.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.ConverterPlugin;

import java.math.BigInteger;

@AutoService(ConverterPlugin.class)
public class BigIntegerToLongConverterPlugin implements ConverterPlugin<BigInteger, Long> {
    @Override
    public String type() {
        return "bigint-to-long";
    }

    @Override
    public Long convert(BigInteger value) {
        if (value == null) {
            return null;
        }
        return value.longValue();
    }
}
