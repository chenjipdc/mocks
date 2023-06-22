package top.chenjipdc.mocks.config.mock.normal;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class UUIDMockConfig extends MockConfig {

    /**
     * 是否去掉'-'
     */
    private Boolean pretty = Boolean.TRUE;
}
