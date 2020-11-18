package com.thread.pool.task;

import java.util.concurrent.TimeUnit;

public class RunnableTask implements Runnable {
    private String taskName;

    public RunnableTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep((int) (Math.random() * 1000));// 1000毫秒以内的随机数，模拟业务逻辑处理
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-------------这里执行业务逻辑，Runnable TaskName = " + taskName + "-------------");
    }
}
