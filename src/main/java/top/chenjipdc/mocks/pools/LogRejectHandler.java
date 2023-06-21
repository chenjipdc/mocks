package top.chenjipdc.mocks.pools;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chenjipdc@gmail.com
 * @date 2022/4/2 11:32 上午
 */
public class LogRejectHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        System.out.println("处理任务队列已满，拒绝提交新任务!!! => {}" + runnable);
    }
}
