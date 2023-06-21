package top.chenjipdc.mocks.config;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class PoolsConfig {

    /**
     * 核心线程数
     */
    private int corePoolSize = 5;

    /**
     * 队列满后的最大线程数
     */
    private int maxPoolSize = 200;

    /**
     * 默认队列容量
     */
    private int capacity = 50000;

    /**
     * 非核心线程空闲回收时间
     */
    private int keepAliveTime = 0;
    private TimeUnit timeUnit = TimeUnit.MINUTES;

    /**
     * 线程名字前缀
     */
    private String namePrefix = "mocks";

    /**
     * 是否设为后台线程执行，看任务的重要性决定
     */
    private boolean daemon = false;

    /**
     * 线程优先级，看任务重要性决定是否提高优先级
     */
    private int priority = 5;

    /**
     * 是否代理目标类，不开启可能会报错（... but was actually of type 'com.sun.proxy.$Proxy48'）
     */
    private boolean proxyTargetClass = true;

}
