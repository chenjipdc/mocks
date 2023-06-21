package top.chenjipdc.mocks.config.sink;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class SinkConfig {

    /**
     * 批量大小, 默认2000
     */
    private Integer batch = 2000;
}
