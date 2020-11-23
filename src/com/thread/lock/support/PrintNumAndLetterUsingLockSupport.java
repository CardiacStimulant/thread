package com.thread.lock.support;

import java.util.concurrent.locks.LockSupport;

public class PrintNumAndLetterUsingLockSupport {

    private static Thread numThread, letterThread;

    public static void main(String[] args) {
        numThread = new Thread(() -> {
            for (int i = 1; i <= 26; i++) {
                System.out.print(i);
                LockSupport.unpark(letterThread);
                LockSupport.park();
            }
        }, "numThread");

        letterThread = new Thread(() -> {
            for (int i = 0; i < 26; i++) {
                LockSupport.park();
                System.out.print((char) ('A' + i));
                LockSupport.unpark(numThread);
            }
        }, "letterThread");

        numThread.start();
        letterThread.start();
    }
}
