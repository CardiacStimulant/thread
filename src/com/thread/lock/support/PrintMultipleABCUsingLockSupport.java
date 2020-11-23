package com.thread.lock.support;

import java.util.concurrent.locks.LockSupport;

/**
 * 多线程按顺序调用，A->B->C，AA 打印5次，BB打印10次，CC打印15次，重复10次
 * 使用Lock/Condition
 */
public class PrintMultipleABCUsingLockSupport {
    private int times;  // 重复次数
    private int numberA;    // A打印次数
    private int numberB;    // B打印次数
    private int numberC;    // C打印次数
    private int countA;
    private int countB;
    private int countC;
    private static Thread threadA, threadB, threadC;

    private PrintMultipleABCUsingLockSupport(int times, int numberA, int numberB, int numberC) {
        this.times = times;
        this.numberA = numberA;
        this.numberB = numberB;
        this.numberC = numberC;
        this.countB = numberB;
        this.countC = numberC;
    }

    private void print(String name, Thread next) {
        for(int i=1; i<=times;) {
            switch (name) {
                case "A" :
                    while (countA<numberA) {
                        System.out.print(name);
                        countA++;
                    }
                    countB = 0;
                    break;
                case "B":
                    LockSupport.park();
                    while (countB<numberB) {
                        System.out.print(name);
                        countB++;
                    }
                    countC = 0;
                    break;
                case "C" :
                    LockSupport.park();
                    while (countC<numberC) {
                        System.out.print(name);
                        countC++;
                    }
                    countA = 0;
                    break;
                default: break;
            }
            LockSupport.unpark(next);
            if(name.equals("A")) {
                LockSupport.park();
            }
            i++;
        }
    }

    public static void main(String[] args) {
        PrintMultipleABCUsingLockSupport printMultipleABCUsingLockCondition = new PrintMultipleABCUsingLockSupport(3, 2, 3, 4);
        threadA = new Thread(()->printMultipleABCUsingLockCondition.print("A", threadB), "A");
        threadB = new Thread(()->printMultipleABCUsingLockCondition.print("B", threadC), "B");
        threadC = new Thread(()->printMultipleABCUsingLockCondition.print("C", threadA), "C");
        threadA.start();
        threadB.start();
        threadC.start();
    }
}
