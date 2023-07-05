package top.chenjipdc.mocks.config.converter.normal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateConverterConfig {

    /**
     * 日期格式化
     */
    private String format = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时区
     */
    private String timeZoneId = "Asia/Shanghai";
}
