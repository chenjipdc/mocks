package top.chenjipdc.mocks.config.mock.normal;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.RangeMockConfig;

@Getter
@Setter
public class ShortMockConfig extends RangeMockConfig<Short> {

    private Boolean autoincrement = Boolean.FALSE;

}
