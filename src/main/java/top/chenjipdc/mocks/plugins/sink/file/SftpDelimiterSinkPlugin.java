package top.chenjipdc.mocks.plugins.sink.file;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.file.SftpDelimiterSinkConfig;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AutoService(SinkPlugin.class)
public class SftpDelimiterSinkPlugin extends AbstractSinkPlugin<SftpDelimiterSinkConfig> {

    private List<Map<String, Object>> cacheValues = new ArrayList<>();


    private Session session;
    private ChannelSftp sftp;

    @Override
    public String type() {
        return "sftp-delimiter";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                SftpDelimiterSinkConfig.class);

        initConnection();
    }

    @SneakyThrows
    private void initConnection() {
        JSch jsch = new JSch();
        session = jsch.getSession(sinkConfig.getUser(),
                sinkConfig.getHost(),
                sinkConfig.getPort());

        session.setPassword(sinkConfig.getPassword());
        session.setConfig("StrictHostKeyChecking",
                "no");
        if (sinkConfig.getConf() != null) {
            sinkConfig.getConf()
                    .forEach(session::setConfig);
        }

        session.connect(sinkConfig.getSessionTimeout());

        sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect(sinkConfig.getChannelTimeout());
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
            String builder = mappingsConvert(values).values()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(sinkConfig.getDelimiter())) +
                    "\n";
            sftp.put(new ByteArrayInputStream(builder
                            .getBytes(StandardCharsets.UTF_8)),
                    sinkConfig.getPath(),
                    sinkConfig.getAppend() ? 2 : 0);
        }
    }

    @SneakyThrows
    private void batchFlush() {
        StringBuilder builder = new StringBuilder();
        for (Map<String, Object> cacheValue : cacheValues) {
            builder.append(cacheValue.values()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(sinkConfig.getDelimiter())));
            builder.append("\n");
        }
        sftp.put(new ByteArrayInputStream(builder.toString()
                        .getBytes(StandardCharsets.UTF_8)),
                sinkConfig.getPath(),
                sinkConfig.getAppend() ? 2 : 0);
        cacheValues = new ArrayList<>();
    }

    @SneakyThrows
    @Override
    public void stop() {
        if (cacheValues.size() > 0) {
            batchFlush();
        }

        sftp.disconnect();
        session.disconnect();

        super.stop();
    }
}
