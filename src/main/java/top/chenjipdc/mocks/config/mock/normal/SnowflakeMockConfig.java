package top.chenjipdc.mocks.config.mock.normal;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class SnowflakeMockConfig extends MockConfig {

    /**
     * 序列号
     */
    private Long datacenterId;

    /**
     * 工作机器id
     */
    private Long workerId;
}
