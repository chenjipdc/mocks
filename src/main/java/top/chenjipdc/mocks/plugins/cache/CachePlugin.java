package top.chenjipdc.mocks.plugins.cache;

import java.util.Collection;
import java.util.Map;

public interface CachePlugin<V> {

    /**
     * 缓存池类型
     *
     * @return 类型
     */
    String type();


    /**
     * 出事化配置
     *
     * @param config 配置
     */
    default void init(String config) {

    }

    /**
     * 缓存
     *
     * @param value 值
     */
    void cache(Map<String, V> value);

    /**
     * 缓存
     *
     * @param value 值
     */
    void caches(Collection<Map<String, V>> value);

    /**
     * 获取一个值
     *
     * @param random 是否随机
     * @return 值
     */
    Map<String, V> get(boolean random);

    /**
     * 缓存大小
     *
     * @return size
     */
    int size();

    /**
     * 停止
     */
    default void stop() {

    }
}
