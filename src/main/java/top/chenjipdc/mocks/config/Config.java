package top.chenjipdc.mocks.config;

import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Config {

    /**
     * 配置排序
     */
    private Integer order = Integer.MAX_VALUE;

    /**
     * 生成的数据条数
     */
    private long size;

    /**
     * 配置名称
     */
    private String name = "";

    /**
     * mock数据源相关配置
     */
    private List<MocksConfig> mocks;

    /**
     * 写入数据源配置
     */
    private List<SinksConfig> sinks;


    @Getter
    @Setter
    public static class MocksConfig {
        /**
         * mock名称
         */
        private String name = "";

        /**
         * 类型
         */
        private String type;

        /**
         * 字段名称，用于sink的mappings映射字段
         */
        private List<String> columns;

        /**
         * 字段别名，主要用于数据库字段等名字冲突
         */
        private Map<String, String> aliases;

        /**
         * datasource 配置
         */
        private String config = "{}";
    }

    @Getter
    @Setter
    public static class SinksConfig {
        /**
         * sink名称
         */
        private String name = "";

        /**
         * 类型
         */
        private String type;

        /**
         * 写入数据量
         */
        private Long size;

        /**
         * 以size大小循环多少次, 可用于多线程加速写入数据
         */
        private Integer loop;

        /**
         * key为表字段名称, value为数据源ds的mocks的alias
         */
        private Map<String, String> mappings;

        /**
         * 配置
         */
        private String config = "{}";

    }

}
