package com.thread.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程分别打印A，B，C，要求这三个线程一起运行，打印n 次，输出形如“ABCABCABC....”的字符串
 * 使用Lock/Condition方式
 * Condition中的await()方法相当于Object的wait()方法，Condition中的signal()方法相当于Object的notify()方法，
 * Condition中的signalAll()相当于Object的notifyAll()方法。
 * 不同的是，Object中的wait()，notify()，notifyAll()方法是和"同步锁"(synchronized关键字)捆绑使用的；
 * 而Condition是需要与"互斥锁"/"共享锁"捆绑使用的。
 */
public class PrintABCUsingLockCondition {

    private int times;
    private int state;
    private static Lock lock = new ReentrantLock();
    private static Condition c1 = lock.newCondition();
    private static Condition c2 = lock.newCondition();
    private static Condition c3 = lock.newCondition();

    private PrintABCUsingLockCondition(int times) {
        this.times = times;
    }

    public static void main(String[] args) {
        PrintABCUsingLockCondition print = new PrintABCUsingLockCondition(10);
        /*
         * 基本逻辑就是，A对应c1，B对应c2，C对应c3
         * 然后通过控制c1->c2->c3->c1这样的顺序，来控制ABC的循环打印
         */
        new Thread(() -> print.printLetter("A", 0, c1, c2), "A").start();
        new Thread(() -> print.printLetter("B", 1, c2, c3), "B").start();
        new Thread(() -> print.printLetter("C", 2, c3, c1), "C").start();
    }

    private void printLetter(String name, int targetState, Condition current, Condition next) {
        for (int i = 0; i < times; ) {
            lock.lock();
            try {
                while (state % 3 != targetState) {
                    current.await();
                }
                state++;
                i++;
                System.out.print(name);
                next.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
