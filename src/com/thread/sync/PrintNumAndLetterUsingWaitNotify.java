package com.thread.sync;

public class PrintNumAndLetterUsingWaitNotify {
    private static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(PrintNumAndLetterUsingWaitNotify::printer, "numThread").start();
        new Thread(PrintNumAndLetterUsingWaitNotify::printer, "letterThread").start();
    }

    private static void printer() {
        synchronized (lock) {
            for (int i = 0; i < 26; i++) {
                if (Thread.currentThread().getName().equals("numThread")) {
                    //打印数字1-26
                    System.out.print((i + 1));
                    // 唤醒其他在等待的线程
                    lock.notifyAll();
                    try {
                        // 让当前线程释放锁资源，进入wait状态
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (Thread.currentThread().getName().equals("letterThread")) {
                    // 打印字母A-Z
                    System.out.print((char) ('A' + i));
                    // 唤醒其他在等待的线程
                    lock.notifyAll();
                    try {
                        // 让当前线程释放锁资源，进入wait状态
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            lock.notifyAll();
        }
    }
}