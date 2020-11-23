package com.thread.sync;

/**
 * 三个线程分别打印A，B，C，要求这三个线程一起运行，打印n 次，输出形如“ABCABCABC....”的字符串
 * 使用wait/notify
 */
public class PrintABCUsingWaitNotify {

    private int state;
    private int times;
    private static final Object LOCK = new Object();

    private PrintABCUsingWaitNotify(int times) {
        this.times = times;
    }

    public static void main(String[] args) {
        PrintABCUsingWaitNotify printABC = new PrintABCUsingWaitNotify(3);
        new Thread(() -> printABC.printLetter("A", 0), "A").start();
        new Thread(() -> printABC.printLetter("B", 1), "B").start();
        new Thread(() -> printABC.printLetter("C", 2), "C").start();
    }

    private void printLetter(String name, int targetState) {
        /*
         * 核心逻辑：通过预设置的顺序进行相互唤醒，
         * 以本例来说，就是BC线程先进入，等待，A线程进入后，A唤醒B，B唤醒C，C唤醒A。。。循环times次
         */
        for (int i = 0; i < times; i++) {
            synchronized (LOCK) {
                while (state % 3 != targetState) {
                    try {
                        LOCK.wait();    // 非匹配线程，则等待
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                state++;
                System.out.print(name);
                LOCK.notifyAll();   // 唤醒所有线程
            }
        }
    }
}
