package top.chenjipdc.mocks.config.converter.normal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringInsertConverterConfig {
    /**
     * placeholder
     */
    private String placeholder;

    /**
     * 插入的位置，越界了则为拼接
     */
    private int index = 0;

    /**
     * 交换插入对象，默认以placeholder为index插入的对象
     */
    private boolean exchange = false;
}
