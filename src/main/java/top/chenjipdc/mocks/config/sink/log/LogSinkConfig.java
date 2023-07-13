package top.chenjipdc.mocks.config.sink.log;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

@Getter
@Setter
public class LogSinkConfig extends SinkConfig {

    /**
     * json pretty
     */
    private Boolean pretty = Boolean.TRUE;
}
