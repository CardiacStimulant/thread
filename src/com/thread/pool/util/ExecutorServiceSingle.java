package com.thread.pool.util;

import java.util.concurrent.*;

/**
 * 单例模式的线程池
 */
public class ExecutorServiceSingle {
    // 创建默认的线程池，核心线程10个,最大线程20个，等待队列10个，非核心线程空闲时间10秒
    private static ExecutorService executorService = new ThreadPoolExecutor(10, 20,
            10L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10));

    //不可实例化
    private ExecutorServiceSingle() {}

    private static ExecutorServiceSingle instance = null;

    // 获取对象
    public static ExecutorServiceSingle getInstance() {
        //单例
        if (instance == null) {
            instance = new ExecutorServiceSingle();
        }
        return instance;
    }

    /**
     * 设置ThreadPoolExecutor线程池
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    public void setThreadPoolExecutor(int corePoolSize,
                                      int maximumPoolSize,
                                      long keepAliveTime,
                                      TimeUnit unit,
                                      BlockingQueue<Runnable> workQueue) {
        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
