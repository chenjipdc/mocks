package top.chenjipdc.mocks.config.sink.mq;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

@Getter
@Setter
public class RocketMqSinkConfig extends SinkConfig {

    /**
     * host:port
     */
    private String endpoint = "localhost:9876";

    /**
     * topic
     */
    private String topic = "sink";

    /**
     * group
     */
    private String group = "DEFAULT";

    /**
     * tag
     */
    private String tag = "*";


    /**
     * keys
     */
    private String keys;
}
