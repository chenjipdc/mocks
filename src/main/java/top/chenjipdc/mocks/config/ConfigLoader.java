package top.chenjipdc.mocks.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConfigLoader {

    public List<Config> getConfigs() throws IOException {

        URL resource = this.getClass()
                .getClassLoader()
                .getResource("mocks.json");
        byte[] allBytes = Files.readAllBytes(Paths.get(resource
                .getFile()));

        return JSON.parseArray(allBytes,
                Config.class);
    }

    public PoolsConfig getPoolsConfig() throws IOException {
        URL resource = this.getClass()
                .getClassLoader()
                .getResource("pools-sink.json");
        byte[] allBytes = Files.readAllBytes(Paths.get(resource
                .getFile()));
        return JSONObject.parseObject(new String(allBytes,
                        StandardCharsets.UTF_8),
                PoolsConfig.class);
    }

}
