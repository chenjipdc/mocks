package top.chenjipdc.mocks.plugins.mock.file;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.file.FileJsonMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.io.File;
import java.util.*;

@AutoService(MockPlugin.class)
public class FileJsonMockPlugin extends AbstractMockPlugin<Object> {

    private final List<Map<String, Object>> values = new ArrayList<>();

    @Override
    public String type() {
        return "file-json";
    }

    @SneakyThrows
    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        FileJsonMockConfig mockConfig = JSONObject.parseObject(config.getConfig(),
                FileJsonMockConfig.class);

        File file = new File(mockConfig.getPath());
        if (!file.exists() && !file.isFile()) {
            throw new RuntimeException("文件不存在：" + mockConfig.getPath());
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String json = scanner.nextLine();
            JSONObject jsonObject = JSONObject.parseObject(json,
                    JSONObject.class);

            Map<String, Object> map = new HashMap<>();
            for (String column : columns) {
                map.put(column,
                        jsonObject.get(column));
            }
            values.add(map);

            if (mockConfig.getLimit() != null && values.size() > mockConfig.getLimit()) {
                break;
            }
        }
        scanner.close();
    }

    @Override
    public Map<String, Object> value() {
        return values.get(NumericUtils.nextInt(values.size()));
    }
}
