package com.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * N个线程循环打印1~n数字
 * 使用Lock
 */
public class LoopPrintNumberUsingLock {
    private int count;
    private int limit;
    private static final int THREAD_COUNT = 4;  // 线程数
    private static final Lock LOCK = new ReentrantLock();

    private LoopPrintNumberUsingLock(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void loopPrint(int index, String name) {
        while (count<limit) {
            LOCK.lock();
            try {
                if (count%THREAD_COUNT == index && count<limit) {
                    System.out.println(String.format("%s：%d", name, count++));
                }
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                LOCK.unlock();
            }
        }
    }

    public static void main(String[] args) {
        LoopPrintNumberUsingLock loopPrintNumberUsingLockCondition = new LoopPrintNumberUsingLock(0, 10);
        for(int i=0; i<THREAD_COUNT; i++) {
            final int index = i;
            new Thread(() -> loopPrintNumberUsingLockCondition.loopPrint(index, "thread" + index)).start();
        }
    }
}
