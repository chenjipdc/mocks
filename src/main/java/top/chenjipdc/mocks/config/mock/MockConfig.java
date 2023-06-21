package top.chenjipdc.mocks.config.mock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MockConfig {
    /**
     * 是否唯一, 这个会额外耗时
     */
    private Boolean unique = false;

}
