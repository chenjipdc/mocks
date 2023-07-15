package top.chenjipdc.mocks.plugins.mock.file;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.file.SftpJsonMockConfig;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class SftpJsonMockPlugin extends AbstractMockPlugin<Object, SftpJsonMockConfig> {

    @Override
    public String type() {
        return "sftp-json";
    }

    @SneakyThrows
    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                SftpJsonMockConfig.class);

        init();
    }

    @SneakyThrows
    private void init() {
        JSch jsch = new JSch();
        Session session = jsch.getSession(mockConfig.getUser(),
                mockConfig.getHost(),
                mockConfig.getPort());

        session.setPassword(mockConfig.getPassword());
        session.setConfig("StrictHostKeyChecking",
                "no");
        if (mockConfig.getConf() != null) {
            mockConfig.getConf()
                    .forEach(session::setConfig);
        }

        session.connect(mockConfig.getSessionTimeout());

        ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect(mockConfig.getChannelTimeout());


        try (InputStream inputStream = sftp.get(mockConfig.getPath())) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                int count = 0;
                String line;

                do {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    JSONObject jsonObject = JSONObject.parseObject(line,
                            JSONObject.class);

                    Map<String, Object> map = new LinkedHashMap<>();
                    for (String column : columns) {
                        Object object = jsonObject.get(column);
                        if (object instanceof BigInteger) {
                            object = Long.parseLong(object.toString());
                        }
                        map.put(aliases.get(column),
                                object);
                    }

                    cachePlugin.cache(map);
                    count++;
                } while (mockConfig.getLimit() == null || count < mockConfig.getLimit());
            }
        }finally {
            sftp.disconnect();
            session.disconnect();
        }
    }
}
