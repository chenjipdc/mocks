package top.chenjipdc.mocks.plugins.filter;

import java.util.Map;

public interface FilterPlugin {

    /**
     * 插件类型
     *
     * @return type
     */
    String type();

    /**
     * 初始化
     *
     * @param config 插件配置
     */
    default void init(String config) {

    }

    /**
     * 是否需要过滤
     *
     * @param value 值
     * @return true/不要过滤 false/需要过滤
     */
    boolean filter(Map<String, Object> value);

    /**
     * 停止
     */
    default void stop() {

    }
}
