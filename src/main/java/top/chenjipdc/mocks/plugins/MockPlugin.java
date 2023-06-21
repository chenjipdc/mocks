package top.chenjipdc.mocks.plugins;


import java.util.Map;

public interface MockPlugin<T> extends MockLeftCycle {

    /**
     * @return type
     */
    String type();

    /**
     * key: column
     * value: value
     *
     * @return map
     */
    Map<String, T> value();
}
