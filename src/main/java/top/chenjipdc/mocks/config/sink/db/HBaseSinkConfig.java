package top.chenjipdc.mocks.config.sink.db;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

import java.util.List;
import java.util.Map;

/**
 * 使用hset存储
 */
@Getter
@Setter
public class HBaseSinkConfig extends SinkConfig {

    private String quorum = "192.168.78.20,192.168.78.21,192.168.78.22";

    private int port = 2181;

    /**
     * 表名
     */
    private String table = "sink";

    /**
     * rowKey
     */
    private String rowKey;

    /**
     * 不能为空
     * key：列族名称
     * value: 列族下的列
     */
    private Map<String, List<String>> families;


}
