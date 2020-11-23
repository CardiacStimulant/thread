package com.thread.sync;

/**
 * N个线程循环打印1~n数字
 * 使用Lock/Condition
 */
public class LoopPrintNumberUsingWaitNotify {
    private int count;
    private int limit;
    private static final int THREAD_COUNT = 3;  // 线程数
    private static final Object LOCK = new Object();

    private LoopPrintNumberUsingWaitNotify(int count, int limit) {
        this.count = count;
        this.limit = limit;
    }

    private void loopPrint(int index, String name) {
        while (count<limit) {
            synchronized (LOCK) {
                try {
                    while (count%THREAD_COUNT != index) {
                        // 防止陷入死循环
                        if(!(count<limit)) {
                            break;
                        }
                        LOCK.wait();
                    }
                    // 防止线程被唤醒后，count已经大于等于limit
                    if(!(count<limit)) {
                        LOCK.notifyAll();
                        break;
                    }
                    System.out.println(String.format("%s：%d", name, count++));
                    LOCK.notifyAll();
                } catch (InterruptedException e) {
                    e.getStackTrace();
                }
                LOCK.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        LoopPrintNumberUsingWaitNotify loopPrintNumberUsingLockCondition = new LoopPrintNumberUsingWaitNotify(0, 10);
        for(int i=0; i<THREAD_COUNT; i++) {
            final int index = i;
            new Thread(() -> loopPrintNumberUsingLockCondition.loopPrint(index, "thread" + index)).start();
        }
    }
}
