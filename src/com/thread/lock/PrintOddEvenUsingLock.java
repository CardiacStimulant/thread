package com.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程交替打印0~100的奇偶数
 * 使用Lock
 */
public class PrintOddEvenUsingLock {
    private int count;
    private int limit;
    private static final Lock lock = new ReentrantLock();

    private PrintOddEvenUsingLock(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void print(int targetState, String name) {
        while(count<limit) {
            lock.lock();
            try {
                if(((targetState == 1 && count % 2 == 0) || (targetState == 2 && count % 2 != 0)) && count<limit) {
                    System.out.println(String.format("线程[%s]打印数字：%d", name, count++));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        PrintOddEvenUsingLock printOddEvenUsingLockCondition = new PrintOddEvenUsingLock(1, 10);
        new Thread(() -> printOddEvenUsingLockCondition.print(1, "odd"), "odd").start();
        new Thread(() -> printOddEvenUsingLockCondition.print(2, "even"), "even").start();
    }
}
