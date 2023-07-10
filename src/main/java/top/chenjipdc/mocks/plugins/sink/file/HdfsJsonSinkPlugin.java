package top.chenjipdc.mocks.plugins.sink.file;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.file.HdfsJsonSinkConfig;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AutoService(SinkPlugin.class)
public class HdfsJsonSinkPlugin extends AbstractSinkPlugin<HdfsJsonSinkConfig> {

    private FSDataOutputStream outputStream;

    private List<Map<String, Object>> cacheValues = new ArrayList<>();

    @Override
    public String type() {
        return "hdfs-json";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                HdfsJsonSinkConfig.class);


        Configuration conf = new Configuration();
        if (sinkConfig.getConf() != null) {
            sinkConfig.getConf().forEach(conf::set) ;
        }

        FileSystem fileSystem = FileSystem.get(new URI(sinkConfig.getUri()),
                conf,
                sinkConfig.getUser());

        Path file = new Path(sinkConfig.getPath());

        if (sinkConfig.getAppend()) {
            if (!fileSystem.exists(file)) {
                fileSystem.createNewFile(file);
            }
            outputStream = fileSystem.append(file);
        } else {
            outputStream = fileSystem.create(file, true);
        }
    }

    @SneakyThrows
    @Override
    public void sink(Map<String, Object> values) {
        if (sinkConfig.getBatch() != null && sinkConfig.getBatch() > 1) {
            if (cacheValues.size() >= sinkConfig.getBatch()) {
                batchFlush();
            } else {
                cacheValues.add(mappingsConvert(values));
            }
        } else {
            outputStream.write(JSONObject.toJSONString(mappingsConvert(values))
                    .getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }

    @SneakyThrows
    private void batchFlush() {
        StringBuilder builder = new StringBuilder();
        for (Map<String, Object> cacheValue : cacheValues) {
            builder.append(JSONObject.toJSONString(cacheValue));
            builder.append("\n");
        }
        outputStream.write(builder.toString()
                .getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        cacheValues = new ArrayList<>();
    }

    @SneakyThrows
    @Override
    public void stop() {
        if (cacheValues.size() > 0) {
            batchFlush();
        }

        outputStream.close();

        super.stop();
    }
}
