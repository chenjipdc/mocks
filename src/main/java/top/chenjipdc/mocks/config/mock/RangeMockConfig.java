package top.chenjipdc.mocks.config.mock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RangeMockConfig<T> extends MockConfig {

    private T start;

    private T end;

}
