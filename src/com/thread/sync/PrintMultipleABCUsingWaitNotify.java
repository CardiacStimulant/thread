package com.thread.sync;

/**
 * 多线程按顺序调用，A->B->C，AA 打印5次，BB打印10次，CC打印15次，重复10次
 * 使用Lock/Condition
 */
public class PrintMultipleABCUsingWaitNotify {
    private int times;  // 重复次数
    private int numberA;    // A打印次数
    private int numberB;    // B打印次数
    private int numberC;    // C打印次数
    private int countA;
    private int countB;
    private int countC;
    private final static Object LOCK = new Object();

    private PrintMultipleABCUsingWaitNotify(int times, int numberA, int numberB, int numberC) {
        this.times = times;
        this.numberA = numberA;
        this.numberB = numberB;
        this.numberC = numberC;
        this.countB = numberB;
        this.countC = numberC;
    }

    private void print(String name) {
        for(int i=1; i<=times;) {
            synchronized (LOCK) {
                try {
                    while (("A".equals(name) && countA!=0)
                            || ("B".equals(name) && countB!=0)
                            || ("C".equals(name) && countC!=0)) {
                        LOCK.wait();
                    }
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
                    LOCK.notifyAll();
                    i++;
                } catch (InterruptedException e) {
                    e.getStackTrace();
                } finally {
                    LOCK.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        PrintMultipleABCUsingWaitNotify printMultipleABCUsingLockCondition = new PrintMultipleABCUsingWaitNotify(3, 2, 3, 4);
        new Thread(()->printMultipleABCUsingLockCondition.print("A"), "A").start();
        new Thread(()->printMultipleABCUsingLockCondition.print("B"), "B").start();
        new Thread(()->printMultipleABCUsingLockCondition.print("C"), "C").start();
    }
}
