package com.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintNumberAndLetterUsingLock {
    private static final Lock LOCK = new ReentrantLock();
    private static String currentThreadName = "number";
    private static int state = 0;

    private void print(String name) {

        while (state < 26) {
            LOCK.lock();
            try {
                if (name.equals(currentThreadName)) {
                    switch (name) {
                        case "number":
                            if(state < 26) {
                                System.out.print(state + 1);
                                currentThreadName = "letter";
                            }
                            break;
                        case "letter":
                            if(state < 26) {
                                System.out.print((char) ('A' + state));
                                currentThreadName = "number";
                                state++;
                            }
                            break;
                        default:
                            break;
                    }
                }
            } catch (IllegalMonitorStateException e) {
                e.getStackTrace();
            } finally {
                LOCK.unlock();
            }
        }
    }

    public static void main(String[] args) {
        PrintNumberAndLetterUsingLock printNumberAndLetterUsingLockCondition = new PrintNumberAndLetterUsingLock();
        new Thread(() -> printNumberAndLetterUsingLockCondition.print("number"), "number").start();
        new Thread(() -> printNumberAndLetterUsingLockCondition.print("letter"), "letter").start();
    }
}
