package com.thread.sync;

/**
 * 两个线程交替打印0~100的奇偶数
 * 使用wait/notify
 */
public class PrintOddEvenUsingWaitNotify {
    private int count;
    private int limit;
    private static final Object LOCK = new Object();

    private PrintOddEvenUsingWaitNotify(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void print() {
        /*
         * 核心逻辑：通过预设置的顺序进行相互唤醒，
         * 以本例来说，odd进入，先打印后唤醒even，odd等待，even打印后唤醒odd，even等待...循环到预设最大值
         */
        synchronized (LOCK) {
            while(count<=limit) {
                System.out.println(String.format("线程[%s]打印数字[%d]", Thread.currentThread().getName(), count++));
                try {
                    LOCK.notifyAll();
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LOCK.notifyAll();
        }
    }

    public static void main(String[] args) {
        PrintOddEvenUsingWaitNotify printOddEvenUsingWaitNotify = new PrintOddEvenUsingWaitNotify(0,10);
        new Thread(printOddEvenUsingWaitNotify::print, "odd").start();
        new Thread(printOddEvenUsingWaitNotify::print, "even").start();
    }
}
