package top.chenjipdc.mocks.config.mock.file;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class FileJsonMockConfig extends MockConfig {

    /**
     * 文件路径
     */
    private String path;

    /**
     * 最多读取多少条
     */
    private Integer limit;
}
