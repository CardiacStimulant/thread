# thread and thread pool
## pool
test thread and thread pool
code: com.thread.pool
example: ThreadPoolExecutor

## thread
介绍了使用多个线程实现有序打印（也就是实际中遇到的线程按照一定的顺序执行的问题），主要使用了：lock，wait/notify，Lock/Condition，Semaphore，LockSupport
题目：
    1. 三个线程分别打印A，B，C，要求这三个线程一起运行，打印n 次，输出形如“ABCABCABC....”的字符串
    2. 两个线程交替打印0~100的奇偶数
    3. 通过N个线程顺序循环打印从0至100
    4. 多线程按顺序调用，A->B->C，AA 打印5次，BB打印10次，CC打印15次，重复10次
    5. 用两个线程，一个输出字母，一个输出数字，交替输出 1A2B3C4D...26Z
总结：
    使用lock，wait/notify，Lock/Condition，其实都会存在一个锁的抢夺过程，如果抢锁的的线程数量足够大，就会出现很多线程抢到了锁但不该自己执行，然后就又解锁或wait()这种操作，这样其实是有些浪费资源的。
    所以使用Semaphore和LockSupport更佳。
    这类问题，解决方式不止这些，还会有 join、CountDownLatch、也有放在队列里解决的，思路有很多
    在使用多线程时，需要注意，在循环中等待/休眠的线程，唤醒之后，可能会已经不满足循环条件了，需要添加一些兼容的判断代码，例子在此项目中很多，可以参照看看
代码：
    wait/notify：com.thread.sync
    Lock：com.thread.lock
    Lock/Condition：com.thread.lock.condition
    Lock/LockSupport：com.thread.lock.support
    semaphore：com.thread.semaphore
参考：https://developer.aliyun.com/article/776793?spm=a2c6h.12873581.0.0.5a87767dTYYAZ2&groupCode=othertech
