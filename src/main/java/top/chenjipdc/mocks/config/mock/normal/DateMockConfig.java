package top.chenjipdc.mocks.config.mock.normal;

import lombok.Getter;
import lombok.Setter;
import top.chenjipdc.mocks.config.mock.RangeMockConfig;

import java.util.Date;

@Getter
@Setter
public class DateMockConfig extends RangeMockConfig<Date> {

    /**
     * 日期格式化
     */
    private String format = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时区
     */
    private String timeZoneId = "Asia/Shanghai";

}
