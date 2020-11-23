package com.thread.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程交替打印0~100的奇偶数
 * 使用Lock/Condition
 */
public class PrintOddEvenUsingLockCondition {
    private int count;
    private int limit;
    private static final Lock lock = new ReentrantLock();
    private static Condition odd = lock.newCondition();
    private static Condition even = lock.newCondition();

    private PrintOddEvenUsingLockCondition(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void print(int targetState, String name, Condition current, Condition next) {
        /*
         * 注意：由于我们的判断条件是count<limit，那么我们再count=9的时候是最后一次循环
         * 当count=9的时候，此时线程odd和even可能都在校验while循环中，当even满足条件，跳出校验循环后
         * 打印了数字9，之后会激活odd线程，那么此时，就会出现：“线程[odd]打印数字：10”
         * 这与count<limit判断不符，所以可以在校验while后，再加上count>=limit的条件
         */
        while(count<limit) {
            lock.lock();
            try {
                // 校验while
                while ((targetState == 1 && count % 2 != 0) || (targetState == 2 && count % 2 == 0)) {
                    current.await();    //注意这里，不要写成wait
                }
                // 防止超过limit这个阈值
                if(count>=limit) {
                    break;
                }
                System.out.println(String.format("线程[%s]打印数字：%d", name, count++));
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        PrintOddEvenUsingLockCondition printOddEvenUsingLockCondition = new PrintOddEvenUsingLockCondition(1, 10);
        new Thread(() -> printOddEvenUsingLockCondition.print(1, "odd", odd, even), "odd").start();
        new Thread(() -> printOddEvenUsingLockCondition.print(2, "even", even, odd), "even").start();
    }
}
