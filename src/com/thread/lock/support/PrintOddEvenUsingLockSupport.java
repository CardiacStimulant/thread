package com.thread.lock.support;

import java.util.concurrent.locks.LockSupport;

/**
 * 两个线程交替打印0~100的奇偶数
 * 使用Lock/Condition
 */
public class PrintOddEvenUsingLockSupport {
    private int count;
    private int limit;
    private static Thread odd, even;

    private PrintOddEvenUsingLockSupport(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void print(int targetState, String name, Thread next) {
        /*
         * 注意：由于我们的判断条件是count<limit，那么我们再count=9的时候是最后一次循环
         * 当count=9的时候，此时线程odd和even可能都在校验while循环中，当even满足条件，跳出校验循环后
         * 打印了数字9，之后会激活odd线程，那么此时，就会出现：“线程[odd]打印数字：10”
         * 这与count<limit判断不符，所以可以在校验while后，再加上count>=limit的条件
         */
        while(count<limit) {
            // 校验while
            while ((targetState == 1 && count % 2 != 0) || (targetState == 2 && count % 2 == 0)) {
                LockSupport.park();    //注意这里，不要写成wait
            }
            // 防止超过limit这个阈值
            if(count<limit) {
                System.out.println(String.format("线程[%s]打印数字：%d", name, count++));
            }
            LockSupport.unpark(next);
        }
    }

    public static void main(String[] args) {
        PrintOddEvenUsingLockSupport printOddEvenUsingLockCondition = new PrintOddEvenUsingLockSupport(1, 10);
        odd = new Thread(() -> printOddEvenUsingLockCondition.print(1, "odd", even), "odd");
        even = new Thread(() -> printOddEvenUsingLockCondition.print(2, "even", odd), "even");
        odd.start();
        even.start();
    }
}
