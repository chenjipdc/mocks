package top.chenjipdc.mocks.plugins.sink.file;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.file.FileDelimiterSinkConfig;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AutoService(SinkPlugin.class)
public class FileDelimiterSinkPlugin extends AbstractSinkPlugin<FileDelimiterSinkConfig> {

    private List<Map<String, Object>> cacheValues = new ArrayList<>();

    private Writer writer;

    @Override
    public String type() {
        return "file-delimiter";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                FileDelimiterSinkConfig.class);

        File file = new File(sinkConfig.getPath());

        // 创建文件
        if (!file.exists() && sinkConfig.getAutoCreated()) {
            boolean created = file.createNewFile();
            if (!created) {
                throw new RuntimeException("创建文件失败：" + sinkConfig.getPath());
            }
        }

        // 获取文件写入
        writer = new FileWriter(file,
                sinkConfig.getAppend());
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
            writer.append(mappingsConvert(values).values()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(sinkConfig.getDelimiter())));
            writer.append("\n");
            writer.flush();
        }
    }

    @SneakyThrows
    private void batchFlush() {
        for (Map<String, Object> cacheValue : cacheValues) {
            writer.append(cacheValue.values()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(sinkConfig.getDelimiter())));
            writer.append("\n");
        }
        writer.flush();
        cacheValues = new ArrayList<>();
    }

    @SneakyThrows
    @Override
    public void stop() {
        super.stop();
        if (cacheValues.size() > 0) {
            batchFlush();
        }
        writer.close();
    }
}
