package top.chenjipdc.mocks.config.mock.file;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class FileTextMockConfig extends MockConfig {

    /**
     * 文件路径
     */
    private String path;

    /**
     * 以什么字符串切割文件内容
     */
    private String split;

}
