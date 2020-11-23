package com.thread.lock.condition;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * N个线程循环打印1~n数字
 * 使用Lock/Condition
 */
public class LoopPrintNumberUsingLockCondition {
    private int count;
    private int limit;
    private static final int THREAD_COUNT = 2;  // 线程数
    private static final Lock LOCK = new ReentrantLock();
    private static List<Condition> conditions = new LinkedList<>(); // Condition集合

    private LoopPrintNumberUsingLockCondition(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void loopPrint(int index, String name, Condition current, Condition next) {
        while (count<limit) {
            LOCK.lock();
            try {
                while (count%THREAD_COUNT != index) {
                    current.await();
                }
                // 防止线程被唤醒后，count已经大于等于limit
                if(!(count<limit)) {
                    next.signal();
                    break;
                }
                System.out.println(String.format("%s：%d", name, count++));
                next.signal();
            } catch (InterruptedException e) {
                e.getStackTrace();
            } finally {
                LOCK.unlock();
            }
        }
    }

    public static void main(String[] args) {
        LoopPrintNumberUsingLockCondition loopPrintNumberUsingLockCondition = new LoopPrintNumberUsingLockCondition(0, 10);
        for(int i=0; i<THREAD_COUNT; i++) {
            conditions.add(LOCK.newCondition());
        }
        for(int i=0; i<THREAD_COUNT; i++) {
            final Condition currentCondition = conditions.get(i);
            final Condition nextCondition = i==THREAD_COUNT-1 ? conditions.get(0) : conditions.get(i+1);
            final int index = i;
            new Thread(() -> loopPrintNumberUsingLockCondition.loopPrint(index, "thread" + index, currentCondition, nextCondition)).start();
        }
    }
}
