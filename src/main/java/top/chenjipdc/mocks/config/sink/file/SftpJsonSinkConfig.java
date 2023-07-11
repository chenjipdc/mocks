package top.chenjipdc.mocks.config.sink.file;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.sink.SinkConfig;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SftpJsonSinkConfig extends SinkConfig {

    /**
     * host
     */
    private String host = "localhost";

    /**
     * port
     */
    private int port = 22;

    /**
     * user
     */
    private String user = "";

    /**
     * password
     */
    private String password = "";

    /**
     * 会话超时时间，毫秒
     */
    private int sessionTimeout = 10000;

    /**
     * 通道超时时间，毫秒
     */
    private int channelTimeout = 10000;

    /**
     * 文件path
     */
    private String path;

    /**
     * false覆盖原文件，true追加
     */
    private Boolean append = Boolean.FALSE;

    /**
     * session conf
     */
    private Map<String, String> conf;

}
