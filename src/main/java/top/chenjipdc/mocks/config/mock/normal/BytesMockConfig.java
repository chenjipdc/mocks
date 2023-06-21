package top.chenjipdc.mocks.config.mock.normal;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.MockConfig;

@Getter
@Setter
public class BytesMockConfig extends MockConfig {

    /**
     * 字节长度
     */
    private Integer length = 100;

}
