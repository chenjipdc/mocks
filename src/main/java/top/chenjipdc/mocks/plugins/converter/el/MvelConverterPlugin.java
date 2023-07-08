package top.chenjipdc.mocks.plugins.converter.el;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import top.chenjipdc.mocks.config.converter.el.MvelConverterConfig;
import top.chenjipdc.mocks.plugins.converter.ConverterPlugin;

import java.io.Serializable;

@AutoService(ConverterPlugin.class)
public class MvelConverterPlugin implements ConverterPlugin<Object, Object> {

    private MvelConverterConfig converterConfig;

    private Serializable expression;

    @Override
    public String type() {
        return "mvel";
    }

    @Override
    public void init(String config) {
        converterConfig = JSONObject.parseObject(config,
                MvelConverterConfig.class);

        expression = MVEL.compileExpression(converterConfig.getExpression());
    }

    @Override
    public Object convert(Object value) {
        VariableResolverFactory context = new MapVariableResolverFactory();
        context.createVariable(converterConfig.getName(),
                value);
        return MVEL.executeExpression(expression,
                context);
    }
}
