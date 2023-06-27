package top.chenjipdc.mocks.config.sink.mq;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

@Getter
@Setter
public class KafkaSinkConfig extends SinkConfig {

    /**
     * host:port
     */
    private String endpoint = "localhost:9092";

    /**
     * topic
     */
    private String topic = "sink";

    /**
     * acks
     */
    private String acks = "0";
}
