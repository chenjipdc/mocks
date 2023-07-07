package top.chenjipdc.mocks.plugins.mock;

import top.chenjipdc.mocks.config.Config;

public interface MockLeftCycle {

    void init(Config.MocksConfig config);

    default void stop() {
    }

}
