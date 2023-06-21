package top.chenjipdc.mocks.config.mock.normal;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class IpMockConfig extends MockConfig {

    /**
     * 生成的ip类型
     */
    private Type type;

    public enum Type {
        V4,
        V4_WAN,

        V4_LAN,

        V4_LAN_A,

        V4_LAN_B,

        V4_LAN_C,
    }

}
