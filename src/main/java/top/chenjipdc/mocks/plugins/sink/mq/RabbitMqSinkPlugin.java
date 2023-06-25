package top.chenjipdc.mocks.plugins.sink.mq;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.mq.RabbitMqSinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.io.IOException;
import java.util.Map;

@AutoService(SinkPlugin.class)
public class RabbitMqSinkPlugin extends AbstractSinkPlugin {

    private RabbitMqSinkConfig sinkConfig;

    private Connection connection;

    private Channel channel;

    @Override
    public String type() {
        return "rabbitmq";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                RabbitMqSinkConfig.class);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(sinkConfig.getHost());
        factory.setPort(sinkConfig.getPort());
        if (sinkConfig.getUsername() != null && sinkConfig.getPassword() != null) {
            factory.setUsername(sinkConfig.getUsername());
            factory.setPassword(sinkConfig.getPassword());
        }

        connection = factory.newConnection();
        channel = connection.createChannel();

        try {
            declare();
        } catch (Exception e) {
            connection.close();
            throw new RuntimeException(e);
        }
    }

    private void declare() throws IOException {
        // 声明queue
        RabbitMqSinkConfig.QueueConfig queueConfig = sinkConfig.getQueueConfig();
        channel.queueDeclare(queueConfig.getName(),
                queueConfig.getDurable(),
                queueConfig.getExclusive(),
                queueConfig.getAutoDelete(),
                queueConfig.getArguments());

        // 声明exchange
        RabbitMqSinkConfig.ExchangeConfig exchangeConfig = sinkConfig.getExchangeConfig();
        if (exchangeConfig != null) {
            channel.exchangeDeclare(exchangeConfig.getName(),
                    exchangeConfig.getType(),
                    exchangeConfig.getDurable(),
                    exchangeConfig.getAutoDelete(),
                    exchangeConfig.getInterval(),
                    exchangeConfig.getArguments());
        }

        // 自动绑定exchange跟queue
        RabbitMqSinkConfig.BindConfig bindConfig = sinkConfig.getBindConfig();
        if (bindConfig != null) {
            String routingKey = bindConfig.getRoutingKey();
            if (routingKey == null) {
                routingKey = bindConfig.getQueue();
            }
            channel.queueBind(bindConfig.getQueue(),
                    bindConfig.getExchange(),
                    routingKey);
        }
    }

    @SneakyThrows
    @Override
    public void sink(Map<String, Object> values) {
        String exchange = "";
        if (sinkConfig.getExchangeConfig() != null) {
            exchange = sinkConfig.getExchangeConfig()
                    .getName();
        }
        String routingKey = sinkConfig.getQueueConfig()
                .getName();
        if (sinkConfig.getBindConfig() != null) {
            if (sinkConfig.getBindConfig()
                    .getRoutingKey() != null) {
                routingKey = sinkConfig.getBindConfig()
                        .getRoutingKey();
            } else if (sinkConfig.getBindConfig()
                    .getQueue() != null) {
                routingKey = sinkConfig.getBindConfig()
                        .getQueue();
            }
        }
        channel.basicPublish(exchange,
                routingKey,
                null,
                JSONObject.toJSONString(mappingsConvert(values))
                        .getBytes());
    }

    @SneakyThrows
    @Override
    public void stop() {
        super.stop();
        channel.close();
        connection.close();
    }

    @Override
    public String logPrefix() {
        return "rabbitmq";
    }
}
