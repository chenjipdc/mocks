package top.chenjipdc.mocks.config.mock.normal;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.RangeMockConfig;

@Getter
@Setter
public class IntMockConfig extends RangeMockConfig<Integer> {

    private Boolean autoincrement = Boolean.FALSE;

}
