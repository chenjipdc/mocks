package top.chenjipdc.mocks.plugins.converter.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.converter.normal.StringReplaceConverterConfig;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringReplaceConverterPlugin implements ConverterPlugin<String, String> {

    private StringReplaceConverterConfig converterConfig;

    @Override
    public String type() {
        return "string-replace";
    }

    @Override
    public void init(String config) {
        converterConfig = JSONObject.parseObject(config,
                StringReplaceConverterConfig.class);
    }

    @Override
    public String convert(String value) {
        if (value == null) {
            return null;
        }
        return value.replace(converterConfig.getPlaceholder(),
                converterConfig.getReplacement());
    }
}
