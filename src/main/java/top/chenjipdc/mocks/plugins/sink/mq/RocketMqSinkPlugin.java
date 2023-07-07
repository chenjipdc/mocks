package top.chenjipdc.mocks.plugins.sink.mq;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.mq.RocketMqSinkConfig;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.util.Map;

@AutoService(SinkPlugin.class)
public class RocketMqSinkPlugin extends AbstractSinkPlugin<RocketMqSinkConfig> {

    private DefaultMQProducer producer;

    @Override
    public String type() {
        return "rocketmq";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                RocketMqSinkConfig.class);

        producer = new DefaultMQProducer(sinkConfig.getGroup());
        producer.setNamesrvAddr(sinkConfig.getEndpoint());
        producer.start();
    }

    @SneakyThrows
    @Override
    public void sink(Map<String, Object> values) {
        Message msg = new Message(sinkConfig.getTopic(),
                sinkConfig.getTag(),
                sinkConfig.getKeys(),
                JSONObject.toJSONString(mappingsConvert(values))
                        .getBytes());
        producer.send(msg);
    }

    @Override
    public String logPrefix() {
        return "rocketmq";
    }

    @SneakyThrows
    @Override
    public void stop() {
        super.stop();
        producer.shutdown();
    }
}
