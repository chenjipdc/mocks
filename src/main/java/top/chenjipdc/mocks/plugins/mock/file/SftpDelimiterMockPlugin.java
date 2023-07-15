package top.chenjipdc.mocks.plugins.mock.file;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.file.SftpDelimiterMockConfig;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class SftpDelimiterMockPlugin extends AbstractMockPlugin<Object, SftpDelimiterMockConfig> {

    @Override
    public String type() {
        return "sftp-delimiter";
    }

    @SneakyThrows
    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                SftpDelimiterMockConfig.class);

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
                    String[] strings = line.split(mockConfig.getDelimiter());

                    Map<String, Object> map = new LinkedHashMap<>();
                    for (int i = 0; i < columns.size(); i++) {
                        Object object = strings[i];
                        map.put(aliases.get(columns.get(i)),
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
