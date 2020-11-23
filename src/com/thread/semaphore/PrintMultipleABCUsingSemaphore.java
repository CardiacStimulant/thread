package com.thread.semaphore;

import java.util.concurrent.Semaphore;

/**
 * 多线程按顺序调用，A->B->C，AA 打印5次，BB打印10次，CC打印15次，重复10次
 * 使用Semaphore
 */
public class PrintMultipleABCUsingSemaphore {
    private int times;  // 重复次数
    private int numberA;    // A打印次数
    private int numberB;    // B打印次数
    private int numberC;    // C打印次数
    private int countA;
    private int countB;
    private int countC;
    private static Semaphore semaphoreA = new Semaphore(1);
    private static Semaphore semaphoreB = new Semaphore(0);
    private static Semaphore semaphoreC = new Semaphore(0);

    private PrintMultipleABCUsingSemaphore(int times, int numberA, int numberB, int numberC) {
        this.times = times;
        this.numberA = numberA;
        this.numberB = numberB;
        this.numberC = numberC;
        this.countB = numberB;
        this.countC = numberC;
    }

    private void print(String name, Semaphore current, Semaphore next) {
        for(int i=1; i<=times; i++) {
            try {
                current.acquire();
                switch (name) {
                    case "A" :
                        while (countA<numberA) {
                            System.out.print(name);
                            countA++;
                        }
                        countB = 0;
                        break;
                    case "B":
                        while (countB<numberB) {
                            System.out.print(name);
                            countB++;
                        }
                        countC = 0;
                        break;
                    case "C" :
                        while (countC<numberC) {
                            System.out.print(name);
                            countC++;
                        }
                        countA = 0;
                        break;
                    default: break;
                }
                next.release();
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        PrintMultipleABCUsingSemaphore printMultipleABCUsingLockCondition = new PrintMultipleABCUsingSemaphore(3, 2, 3, 4);
        new Thread(()->printMultipleABCUsingLockCondition.print("A", semaphoreA, semaphoreB), "A").start();
        new Thread(()->printMultipleABCUsingLockCondition.print("B", semaphoreB, semaphoreC), "B").start();
        new Thread(()->printMultipleABCUsingLockCondition.print("C", semaphoreC, semaphoreA), "C").start();
    }
}
