package com.thread.pool;

import com.thread.pool.task.CallableTask;
import com.thread.pool.task.RunnableTask;
import com.thread.pool.util.ExecutorServiceFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 测试线程池
 * 一旦提交的线程数超过当前可用线程数时，就会抛出java.util.concurrent.RejectedExecutionException
 * 程序可以通过等待时间来测试keepAliveTime的作用
 * 如果等待时间小于或等于keepAliveTime时（这里只是个虚值，本地运行是，修改为11秒，结果也一样），那么就会出现java.util.concurrent.RejectedExecutionException
 * 如果将程序中等待10秒修改为15秒，那么就不会出现java.util.concurrent.RejectedExecutionException
 * 因为超过keepAliveTime时，就会将非核心线程移除，但是这里需要注意一下，主线程不一定就已经全部进入空闲状态了，
 * 如果这时还有主线程没有进入空闲状态，还是会出现java.util.concurrent.RejectedExecutionException，所以尽量等待时间长一点
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        /* 单例模式测试 */
//        ExecutorService executorService = ExecutorServiceSingle.getInstance().getExecutorService();
        /* 工厂模式测试 */
        ExecutorService executorService = ExecutorServiceFactory.getInstance().newThreadPoolExecutor();
        // 提交10个线程到线程池（满值）
        for (int i = 0; i < 30; i++) {
            Future<?> future = executorService.submit(new CallableTask(i+""));
        }
        // 程序等待10秒，修改为15秒，那么就不会出现java.util.concurrent.RejectedExecutionException
        Thread.currentThread().sleep(20000);
        System.out.println("主程序结束");
        // 再让执行10个线程
        for (int i = 0; i < 30; i++) {
            executorService.execute(new RunnableTask(i+""));
        }
    }
}
