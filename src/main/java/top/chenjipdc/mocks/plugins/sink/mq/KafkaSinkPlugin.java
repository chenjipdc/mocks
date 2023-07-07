package top.chenjipdc.mocks.plugins.sink.mq;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.mq.KafkaSinkConfig;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.util.Map;
import java.util.Properties;

@AutoService(SinkPlugin.class)
public class KafkaSinkPlugin extends AbstractSinkPlugin<KafkaSinkConfig> {

    private KafkaProducer<String, String> producer;

    @Override
    public String type() {
        return "kafka";
    }

    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                KafkaSinkConfig.class);

        producer = new KafkaProducer<>(kafkaProperties());
    }

    private Properties kafkaProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers",
                sinkConfig.getEndpoint());
        properties.put("acks",
                sinkConfig.getAcks());
        properties.put("batch.size",
                sinkConfig.getBatch());

        properties.put("enable.auto.commit",
                true);

        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        return properties;
    }

    @Override
    public void sink(Map<String, Object> values) {
        ProducerRecord<String, String> record = new ProducerRecord<>(sinkConfig.getTopic(),
                JSONObject.toJSONString(mappingsConvert(values)));
        producer.send(record);
    }

    @Override
    public void stop() {
        super.stop();

        producer.flush();
        producer.close();
    }

    @Override
    public String logPrefix() {
        return "kafka";
    }
}
