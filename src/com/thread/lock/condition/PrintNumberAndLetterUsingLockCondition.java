package com.thread.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintNumberAndLetterUsingLockCondition {
    private static final Lock LOCK = new ReentrantLock();
    private static Condition number = LOCK.newCondition();
    private static Condition letter = LOCK.newCondition();

    private void print(String name, Condition current, Condition next) {
        LOCK.lock();
        try {
            int i = 0;
            while (i<26) {
                switch (name) {
                    case "number":
                        System.out.print(i+1);
                        break;
                    case "letter":
                        System.out.print((char) ('A' + i));
                        break;
                    default: break;
                }
                next.signal();
                current.await();
                i++;
            }
            next.signal();
        } catch (InterruptedException e) {
            e.getStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public static void main(String[] args) {
        PrintNumberAndLetterUsingLockCondition printNumberAndLetterUsingLockCondition = new PrintNumberAndLetterUsingLockCondition();
        new Thread(()->printNumberAndLetterUsingLockCondition.print("number", number, letter), "number").start();
        new Thread(()->printNumberAndLetterUsingLockCondition.print("letter", letter, number), "letter").start();
    }
}
