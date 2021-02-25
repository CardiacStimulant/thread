package com.thread.sync.upgrade;

import lombok.Data;
import org.openjdk.jol.info.ClassLayout;

@Data
public class SynchronizedLockUpgrade {
    private int id;
    private String name;

    public static void main(String[] args) throws Exception {
        // 默认情况下，需要4秒才会开启偏向锁
        SynchronizedLockUpgrade test = new SynchronizedLockUpgrade();
        System.out.println("无状态：" + ClassLayout.parseInstance(test).toPrintable());
        // 等待5秒，然后再次创建对象，验证是否开启了偏向锁
        Thread.sleep(5000);
        SynchronizedLockUpgrade test2 = new SynchronizedLockUpgrade();
        System.out.println("偏向锁开启：" + ClassLayout.parseInstance(test2).toPrintable());
        // 偏向锁加锁，这是需要注意mark word
        synchronized (test2) {
            System.out.println("偏向锁加锁：" + ClassLayout.parseInstance(test2).toPrintable());
        }
        // 偏向锁解锁后再次观察mark word
        System.out.println("偏向锁解锁：" + ClassLayout.parseInstance(test2).toPrintable());
        // 模拟相同线程请求相同的synchronized代码块
        synchronized (test2) {
            System.out.println("同一线程继续访问synchronized的对象：" + ClassLayout.parseInstance(test2).toPrintable());
        }
        // 启动第二个线程，验证轻量级锁
        new Thread(() -> {
            System.out.println("第二个线程启动");
            synchronized (test2) {
                System.out.println("轻量级锁：" + ClassLayout.parseInstance(test2).toPrintable());
                try {
                    // 模拟该线程还没有执行完成，锁还没有释放
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // 保证第三个线程在第二个线程之后启动
        Thread.sleep(500);
        // 启动第三个线程在第二个线程还没有释放锁的情况下，升级为重量级锁
        new Thread(() -> {
            System.out.println("第三个线程启动");
            synchronized (test2) {
                System.out.println("重量级锁：" + ClassLayout.parseInstance(test2).toPrintable());
            }
        }).start();
    }
}
