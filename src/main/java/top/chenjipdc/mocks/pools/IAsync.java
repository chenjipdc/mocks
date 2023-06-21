package top.chenjipdc.mocks.pools;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池异步任务封装
 *
 * @author chenjipdc@gmail.com
 * @date 2022/4/2 10:23 上午
 */
public interface IAsync {

    /**
     * 直接丢进线程池
     *
     * @param runnable 执行
     */
    void async(Runnable runnable);

    /**
     * 带返回值
     * @param callable call
     * @param <T> 返回值类型
     * @return future
     */
    <T> Future<T> async(Callable<T> callable);

    /**
     * 带返回值
     * @param runnable runnable
     * @param t 返回值
     * @param <T> 返回值类型
     * @return future
     */
    <T> Future<T> async(Runnable runnable, T t);
}
