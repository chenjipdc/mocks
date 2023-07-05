package top.chenjipdc.mocks.plugins.converter.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.converter.normal.StringInsertConverterConfig;
import top.chenjipdc.mocks.config.converter.normal.StringReplaceConverterConfig;
import top.chenjipdc.mocks.plugins.ConverterPlugin;

@AutoService(ConverterPlugin.class)
public class StringInsertConverterPlugin implements ConverterPlugin<String, String> {

    private StringInsertConverterConfig converterConfig;

    @Override
    public String type() {
        return "string-insert";
    }

    @Override
    public void init(String config) {
        converterConfig = JSONObject.parseObject(config,
                StringInsertConverterConfig.class);
    }

    @Override
    public String convert(String value) {
        if (value == null) {
            return null;
        }
        if (converterConfig.getPlaceholder() == null) {
            return value;
        }

        String placeholder = value;
        String insert = converterConfig.getPlaceholder();

        if (converterConfig.isExchange()) {
            placeholder = converterConfig.getPlaceholder();
            insert = value;
        }

        int index = converterConfig.getIndex();

        if (index <= 0) {
            return insert + placeholder;
        } else if (index >= placeholder.length()) {
            return placeholder + insert;
        } else {
            return new StringBuffer(placeholder).insert(index,
                            insert)
                    .toString();
        }
    }
}
