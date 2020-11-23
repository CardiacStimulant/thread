package com.thread.semaphore;

import java.util.concurrent.Semaphore;

/**
 * 三个线程分别打印A，B，C，要求这三个线程一起运行，打印n 次，输出形如“ABCABCABC....”的字符串
 * 使用Semaphore
 */
public class PrintABCUsingSemaphore {
    private int times;
    private static Semaphore semaphoreA = new Semaphore(1); // 只有A 初始信号量为1,第一次获取到的只能是A
    private static Semaphore semaphoreB = new Semaphore(0);
    private static Semaphore semaphoreC = new Semaphore(0);

    private PrintABCUsingSemaphore(int times) {
        this.times = times;
    }

    public static void main(String[] args) {
        PrintABCUsingSemaphore printer = new PrintABCUsingSemaphore(3);
        new Thread(() -> printer.print("A", semaphoreA, semaphoreB), "A").start();
        new Thread(() -> printer.print("B", semaphoreB, semaphoreC), "B").start();
        new Thread(() -> printer.print("C", semaphoreC, semaphoreA), "C").start();
    }

    private void print(String name, Semaphore current, Semaphore next) {
        for (int i = 0; i < times; i++) {
            try {
                current.acquire();  // A获取信号执行,A信号量减1,当A为0时将无法继续获得该信号量
                System.out.print(name);
                next.release();    // B释放信号，B信号量加1（初始为0），此时可以获取B信号量
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
