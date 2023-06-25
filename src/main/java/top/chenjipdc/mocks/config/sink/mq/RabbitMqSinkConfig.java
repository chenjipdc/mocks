package top.chenjipdc.mocks.config.sink.mq;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

import java.util.Map;

@Getter
@Setter
public class RabbitMqSinkConfig extends SinkConfig {

    /**
     * host
     */
    private String host = "localhost";

    /**
     * port
     */
    private int port = 5672;

    /**
     * username
     */
    private String username = "guest";

    /**
     * password
     */
    private String password = "guest";

    /**
     * exchange config
     */
    private ExchangeConfig exchangeConfig;

    /**
     * queue config
     */
    private QueueConfig queueConfig;

    /**
     * bind config
     */
    private BindConfig bindConfig;

    @Getter
    @Setter
    public static class ExchangeConfig {

        private String name = "default";

        private String type = "direct";

        private Boolean autoDelete = Boolean.FALSE;

        private Boolean durable = Boolean.FALSE;

        private Boolean interval = Boolean.FALSE;

        private Map<String, Object> arguments;
    }

    @Getter
    @Setter
    public static class QueueConfig {

        private String name = "mocks-sink";

        private Boolean durable = Boolean.FALSE;

        private Boolean exclusive = Boolean.FALSE;

        private Boolean autoDelete = Boolean.FALSE;

        private Map<String, Object> arguments;
    }

    @Getter
    @Setter
    public static class BindConfig {

        private String exchange = "default";

        private String queue = "mocks-sink";

        private String routingKey;

        private Map<String, Object> arguments;

    }
}
