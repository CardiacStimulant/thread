package com.thread.semaphore;

import java.util.concurrent.Semaphore;

public class PrintNumberAndLetterUsingLSemaphore {
    private static Semaphore semaphoreNumber = new Semaphore(1);
    private static Semaphore semaphoreLetter = new Semaphore(0);
    private static int state = 0;

    private void print(String name, Semaphore current, Semaphore next) {
        while (state < 26) {
            try {
                current.acquire();
                switch (name) {
                    case "number":
                        if (state < 26) {
                            System.out.print(state + 1);
                        }
                        break;
                    case "letter":
                        if (state < 26) {
                            System.out.print((char) ('A' + state));
                        }
                        state++;
                        break;
                    default:
                        break;
                }
                next.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        PrintNumberAndLetterUsingLSemaphore printNumberAndLetterUsingLockCondition = new PrintNumberAndLetterUsingLSemaphore();
        new Thread(() -> printNumberAndLetterUsingLockCondition.print("number", semaphoreNumber, semaphoreLetter), "number").start();
        new Thread(() -> printNumberAndLetterUsingLockCondition.print("letter", semaphoreLetter, semaphoreNumber), "letter").start();
    }
}
