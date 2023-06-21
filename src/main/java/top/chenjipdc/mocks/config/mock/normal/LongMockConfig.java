package top.chenjipdc.mocks.config.mock.normal;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.RangeMockConfig;

@Getter
@Setter
public class LongMockConfig extends RangeMockConfig<Long> {

    /**
     * 是否自增
     */
    private Boolean autoincrement = Boolean.FALSE;

}
