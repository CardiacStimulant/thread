package com.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程分别打印A，B，C，要求这三个线程一起运行，打印n 次，输出形如“ABCABCABC....”的字符串
 * 使用Lock方式
 */
public class PrintABCUsingLock {

    private int times; // 控制打印次数
    private int state;   // 当前状态值：保证三个线程之间交替打印
    private Lock lock = new ReentrantLock();

    private PrintABCUsingLock(int times) {
        this.times = times;
    }

    /**
     *
     * @param name 线程名称
     * @param targetNum 控制顺序
     */
    private void printLetter(String name, int targetNum) {
        /*
         * 核心逻辑，使用times来控制循环次数，将i++和state++放到可执行的条件中
         * 启动线程后，会进行抢锁，但是只有满足条件的线程才会进入打印逻辑
         * 示例分析：
         *      A，B，C线程不管谁先谁后进去，只有A线程进行取模判断时，才能满足条件，打印A
         *      这时i++，控制进入第二次循环，state++控制线程的顺序，这时i=2，state=1，只有B线程传入的targetNum满足取模条件，打印B
         *      同理，i=2，state=2，这时只有C线程满足条件，打印C
         *      ... 循环即可，最终打印：ABC...（重复times次）
         * 我们如果再进入for循环前加上System.out.println(name);可以看出，times为1时，BC经常出现打印多次的情况，times>1是，ABC都会经常打印多次的情况
         * 这样就造成了很多时候，线程其实是在重复进行着lock/unlock，造成了极大的资源损耗
         */
        for (int i = 0; i < times; ) {
            lock.lock();
            try {
                if (state % 3 == targetNum) {   // 取模判断
                    state++;
                    i++;
                    System.out.print(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();  // 一般使用Lock.lock()都放到try catch里面，然后在finally中使用Lock.unlock()防止死锁
            }
        }
    }

    public static void main(String[] args) {
        PrintABCUsingLock loopThread = new PrintABCUsingLock(3);

        new Thread(() -> loopThread.printLetter("B", 1), "B").start();
        new Thread(() -> loopThread.printLetter("A", 0), "A").start();
        new Thread(() -> loopThread.printLetter("C", 2), "C").start();
    }
}
