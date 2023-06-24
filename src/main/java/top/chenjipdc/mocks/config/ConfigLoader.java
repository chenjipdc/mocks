package top.chenjipdc.mocks.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ConfigLoader {

    private static final String CONFIG_FILE_EXTENSION = ".json";

    private static final String MOCKS_CONFIG_DIR = "config/";

    private static final String MOCKS_POOLS_SINK = "pools-sink.json";

    private static final String HOME_CONFIG_PATH = System.getProperty("user.home") + "/.mocks/" + MOCKS_CONFIG_DIR;

    private static final String HOME_POOLS_SINK_PATH = System.getProperty("user.home") + "/.mocks/" + MOCKS_POOLS_SINK;

    public List<Config> getConfigs() {

        List<Config> configs = new ArrayList<>();

        // from home directory
        List<Config> configList = getConfigsFromPath(HOME_CONFIG_PATH);
        if (configList != null && configList.size() > 0) {
            configs.addAll(configList);
        }

        // from config directory
        configList = getConfigsFromPath(jarConfigDir());
        if (configList != null && configList.size() > 0) {
            configs.addAll(configList);
        }

        return configs.stream()
                .sorted((it1, it2) -> {
                    if (it1.getOrder() > it2.getOrder()) {
                        return 1;
                    } else if (it1.getOrder()
                            .equals(it2.getOrder())) {
                        return 0;
                    } else {
                        return -1;
                    }
                })
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private String jarConfigDir() {
        String jarPath = ConfigLoader.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        return jarPath.substring(0,
                jarPath.lastIndexOf("/")) + "/" + MOCKS_CONFIG_DIR;
    }

    @SneakyThrows
    private List<Config> getConfigsFromPath(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        List<Config> configs = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (file.isFile() && file.getName()
                    .endsWith(CONFIG_FILE_EXTENSION)) {
                byte[] allBytes = Files.readAllBytes(Paths.get(file.getPath()));
                List<Config> configList = JSON.parseArray(allBytes,
                        Config.class);
                if (configList != null && configList.size() > 0) {
                    log.info("read file: {}",
                            file.getPath());
                    configs.addAll(configList);
                }
            }
        }
        return configs;
    }

    public PoolsConfig getPoolsConfig() {

        // from home directory
        PoolsConfig config = getPoolsConfigFromPath(HOME_POOLS_SINK_PATH);
        if (config != null) {
            return config;
        }

        // from config directory
        config = getPoolsConfigFromPath(jarConfigDir() + "/" + MOCKS_POOLS_SINK);
        if (config != null) {
            return config;
        }

        return new PoolsConfig();
    }

    @SneakyThrows
    private PoolsConfig getPoolsConfigFromPath(String path) {
        File config = new File(path);
        log.info("PoolsConfig path: {}",
                path);
        if (!config.exists() || !config.isFile()) {
            return null;
        }
        byte[] allBytes = Files.readAllBytes(Paths.get(path));
        return JSONObject.parseObject(new String(allBytes,
                        StandardCharsets.UTF_8),
                PoolsConfig.class);
    }

}
