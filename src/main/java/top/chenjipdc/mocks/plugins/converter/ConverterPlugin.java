package top.chenjipdc.mocks.plugins.converter;

public interface ConverterPlugin<T, R> {
    /**
     * @return 插件类型
     */
    String type();

    /**
     * @param config json
     */
    default void init(String config) {
        
    }

    /**
     * 类型转换
     *
     * @param value 需转换的值
     * @return 转换后的值
     */
    R convert(T value);

    default void stop(){

    }

}
