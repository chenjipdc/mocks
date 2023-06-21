package top.chenjipdc.mocks.pools;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenjipdc@gmail.com
 * @date 2022/4/2 10:38 上午
 */
public class SimpleThreadFactory implements ThreadFactory {


    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;
    final int priority;
    final boolean daemon;

    SimpleThreadFactory(String namePrefix, int priority, boolean daemon) {
        this.namePrefix = namePrefix;
        this.priority = priority;
        this.daemon = daemon;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread()
                        .getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group,
                r,
                this.namePrefix + " - [thread：" + threadNumber.getAndIncrement() + "]",
                0);
        t.setDaemon(this.daemon);
        t.setPriority(this.priority);
        return t;
    }
}
