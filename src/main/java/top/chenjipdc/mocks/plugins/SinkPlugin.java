package top.chenjipdc.mocks.plugins;

import java.util.Map;

/**
 * sink下都是单线程执行
 */
public interface SinkPlugin extends SinkLeftCycle {

    String type();

    void sink(Map<String, Object> values);

}
