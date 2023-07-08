package top.chenjipdc.mocks.config.converter.el;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MvelConverterConfig {

    /**
     * expression表达式里的变量名称，默认：mock
     */
    private String name = "mock";

    /**
     * 表达式
     */
    private String expression;
}
