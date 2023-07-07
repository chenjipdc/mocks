package top.chenjipdc.mocks.plugins.sink;

import top.chenjipdc.mocks.config.Config;

public interface SinkLeftCycle {

    void init(Config.SinksConfig config);

    default void stop() {
    }

}
