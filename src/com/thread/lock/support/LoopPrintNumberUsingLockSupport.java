package com.thread.lock.support;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * N个线程循环打印1~n数字
 * 使用Lock/Condition
 */
public class LoopPrintNumberUsingLockSupport {
    private static int count;
    private static int limit;
    private static final int THREAD_COUNT = 3;  // 线程数
    private static List<Thread> threads = new LinkedList<>(); // Thread集合

    private LoopPrintNumberUsingLockSupport(int count, int limit) {
        LoopPrintNumberUsingLockSupport.count = count;
        LoopPrintNumberUsingLockSupport.limit = limit;
    }

    public static void main(String[] args) {
        new LoopPrintNumberUsingLockSupport(0, 10);
        for(int i=0; i<THREAD_COUNT; i++) {
            final int index = i;
            if(i==0) {
                threads.add(new Thread(()->{
                    for(; count<LoopPrintNumberUsingLockSupport.limit;) {
                        System.out.println(String.format("线程%d：%d", index, count++));
                        LockSupport.unpark(threads.get(index+1));
                        LockSupport.park();
                    }
                }));
            } else if(i==THREAD_COUNT-1) {
                threads.add(new Thread(()->{
                    for(; count<LoopPrintNumberUsingLockSupport.limit;) {
                        LockSupport.park();
                        if(count<LoopPrintNumberUsingLockSupport.limit) {
                            System.out.println(String.format("线程%d：%d", index, count++));
                        }
                        LockSupport.unpark(threads.get(0));
                    }
                }));
            } else {
                threads.add(new Thread(()->{
                    for(; count<LoopPrintNumberUsingLockSupport.limit;) {
                        LockSupport.park();
                        if(count<LoopPrintNumberUsingLockSupport.limit) {
                            System.out.println(String.format("线程%d：%d", index, count++));
                        }
                        LockSupport.unpark(threads.get(index+1));
                    }
                }));
            }
        }
        for(Thread thread : threads) {
            thread.start();
        }
    }
}
