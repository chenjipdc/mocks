package top.chenjipdc.mocks.config.sink.file;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

@Getter
@Setter
public class FileJsonSinkConfig extends SinkConfig {

    /**
     * 文件path
     */
    private String path;

    /**
     * 是否自动创建文件
     */
    private Boolean autoCreated = Boolean.TRUE;

    /**
     * false覆盖原文件，true追加
     */
    private Boolean append = Boolean.FALSE;
}
