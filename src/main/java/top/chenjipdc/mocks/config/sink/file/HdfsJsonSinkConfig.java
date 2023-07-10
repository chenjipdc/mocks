package top.chenjipdc.mocks.config.sink.file;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class HdfsJsonSinkConfig extends SinkConfig {

    /**
     * namenode地址
     */
    private String uri = "hdfs://192.168.78.21:8020";

    /**
     * 使用用户
     */
    private String user = "hadoop";

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

    /**
     * conf
     */
    private Map<String, String> conf = new HashMap<>();
}
