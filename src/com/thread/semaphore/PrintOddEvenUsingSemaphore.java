package com.thread.semaphore;

import java.util.concurrent.Semaphore;

/**
 * 两个线程交替打印0~100的奇偶数
 * 使用Lock
 */
public class PrintOddEvenUsingSemaphore {
    private int count;
    private int limit;
    private static Semaphore semaphoreOdd = new Semaphore(1);
    private static Semaphore semaphoreEven = new Semaphore(0);

    private PrintOddEvenUsingSemaphore(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void print(int targetState, String name, Semaphore current, Semaphore next) {
        while(count<limit) {
            try {
                current.acquire();
                if(((targetState == 1 && count % 2 == 0) || (targetState == 2 && count % 2 != 0)) && count<limit) {
                    System.out.println(String.format("线程[%s]打印数字：%d", name, count++));
                }
                next.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        PrintOddEvenUsingSemaphore printOddEvenUsingLockCondition = new PrintOddEvenUsingSemaphore(1, 10);
        new Thread(() -> printOddEvenUsingLockCondition.print(1, "odd", semaphoreOdd, semaphoreEven), "odd").start();
        new Thread(() -> printOddEvenUsingLockCondition.print(2, "even", semaphoreEven, semaphoreOdd), "even").start();
    }
}
