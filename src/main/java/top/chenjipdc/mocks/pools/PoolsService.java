package top.chenjipdc.mocks.pools;

import lombok.SneakyThrows;
import top.chenjipdc.mocks.config.PoolsConfig;

import java.util.concurrent.*;

/**
 * @author chenjipdc@gmail.com
 * @date 2022/4/2 10:23 上午
 */
public class PoolsService implements IAsync {

    private final PoolsConfig poolsConfig;

    private ThreadPoolExecutor executor;

    public PoolsService(PoolsConfig poolsConfig) {
        this.poolsConfig = poolsConfig;
        init();
    }


    public void init() {
        executor = new ThreadPoolExecutor(poolsConfig.getCorePoolSize(),
                poolsConfig.getMaxPoolSize(),
                poolsConfig.getKeepAliveTime(),
                poolsConfig.getTimeUnit(),
                new LinkedBlockingDeque<>(poolsConfig.getCapacity()),
                new SimpleThreadFactory(poolsConfig.getNamePrefix(),
                        poolsConfig.getPriority(),
                        poolsConfig.isDaemon()),
                new LogRejectHandler());
    }

    public void destroy() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    @SneakyThrows
    public void waitCompleted() {
        // 等待线程池执行完成
        while (executor.getTaskCount() != executor.getCompletedTaskCount()) {
            Thread.sleep(1000);
        }
    }


    public void async(Runnable runnable) {
        executor.execute(runnable);
    }


    public <T> Future<T> async(Callable<T> callable) {
        return executor.submit(callable);
    }


    public <T> Future<T> async(Runnable runnable, T t) {
        return executor.submit(runnable,
                t);
    }
}
