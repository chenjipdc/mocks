package top.chenjipdc.mocks.plugins.mock.file;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.file.FileTextMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@AutoService(MockPlugin.class)
public class FileTextMockPlugin extends AbstractMockPlugin<Object> {

    private final List<Object> values = new ArrayList<>();

    @Override
    public String type() {
        return "file-text";
    }

    @SneakyThrows
    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        FileTextMockConfig mockConfig = JSONObject.parseObject(config.getConfig(),
                FileTextMockConfig.class);

        byte[] allBytes = Files.readAllBytes(Paths.get(mockConfig.getPath()));
        String text = new String(allBytes,
                StandardCharsets.UTF_8);
        values.addAll(Arrays.asList(text.split(mockConfig.getSplit())));
    }

    @Override
    public Map<String, Object> value() {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String column : aliases.values()) {
            map.put(column,
                    values.get(NumericUtils.nextInt(values.size())));
        }
        return map;
    }
}
