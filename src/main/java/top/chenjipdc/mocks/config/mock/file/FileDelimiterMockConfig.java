package top.chenjipdc.mocks.config.mock.file;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class FileDelimiterMockConfig extends MockConfig {

    /**
     * 文件路径
     */
    private String path;

    /**
     * 行内容切割字符串，默认','
     */
    private String delimiter = ",";

    /**
     * 最多读取多少条
     */
    private Integer limit;

}
