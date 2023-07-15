package top.chenjipdc.mocks.config.mock.file;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

import java.util.Map;

@Getter
@Setter
public class SftpDelimiterMockConfig extends MockConfig {

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

    /**
     * session conf
     */
    private Map<String, String> conf;
}
