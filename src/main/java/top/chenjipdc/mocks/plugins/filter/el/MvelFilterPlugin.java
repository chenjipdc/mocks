package top.chenjipdc.mocks.plugins.filter.el;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import org.mvel2.MVEL;
import top.chenjipdc.mocks.config.filter.MvelFilterConfig;
import top.chenjipdc.mocks.plugins.filter.FilterPlugin;

import java.io.Serializable;
import java.util.Map;

@AutoService(FilterPlugin.class)
public class MvelFilterPlugin implements FilterPlugin {

    private MvelFilterConfig filterConfig;

    private Serializable expression;

    @Override
    public String type() {
        return "mvel";
    }

    @Override
    public void init(String config) {
        filterConfig = JSONObject.parseObject(config,
                MvelFilterConfig.class);

        initMvel();
    }

    private void initMvel() {
        expression = MVEL.compileExpression(filterConfig.getExpression());
    }

    @Override
    public boolean filter(Map<String, Object> value) {
        return MVEL.executeExpression(expression,
                value,
                Boolean.class);
    }
}
