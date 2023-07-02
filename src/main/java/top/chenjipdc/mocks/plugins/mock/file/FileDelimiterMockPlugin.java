package top.chenjipdc.mocks.plugins.mock.file;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.file.FileDelimiterMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.io.File;
import java.util.*;

@AutoService(MockPlugin.class)
public class FileDelimiterMockPlugin extends AbstractMockPlugin<Object, FileDelimiterMockConfig> {

    private final List<Map<String, Object>> values = new ArrayList<>();

    @Override
    public String type() {
        return "file-delimiter";
    }

    @SneakyThrows
    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                FileDelimiterMockConfig.class);

        File file = new File(mockConfig.getPath());
        if (!file.exists() && !file.isFile()) {
            throw new RuntimeException("文件不存在：" + mockConfig.getPath());
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] strings = line.split(mockConfig.getDelimiter());

            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 0; i < columns.size(); i++) {
                Object object = strings[i];
                map.put(aliases.get(columns.get(i)),
                        object);
            }
            values.add(JSONObject.parseObject(JSONObject.toJSONString(map), LinkedHashMap.class));

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
