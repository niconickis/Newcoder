package com.nowcoder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by nowcoder on 2016/7/24.
 */
// 是extends不是implements,extends用于(单)继承一个类（class），而implements用于实现一个接口(interface)。
// 在类的声明中，通过关键字extends来创建一个类的子类。一个类通过关键字implements声明自己使用一个或者多个接口。
// extends 是继承某个类, 继承之后可以使用父类的方法, 也可以重写父类的方法;implements 是实现多个接口, 接口的方法一般为空的, 必须重写才能使用.
class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                Thread.sleep(1000);
                System.out.println(String.format("%d:%d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 另一种启动一个线程的方法，集成runnable并重写run;消费者线程从BlockingQueue中取出字符并打印当前线程名字
class Consumer implements Runnable {
    private BlockingQueue<String> q;
    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 多线程用在哪呢？-----异步队列中；生产者将字符加入队列
class Producer implements Runnable {
    private BlockingQueue<String> q;
    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; ++i) {
                Thread.sleep(1000);
                q.put(String .valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MultiThreadTests {
    public static void testThread() {
        // 启动了十个线程，每个线程工作是都从0打印到9；这样的工作每个线程没有调用共用的对象或变量，所以互不影响，不需要锁
        for (int i = 0; i < 10; ++i) {
            //new MyThread(i).start();
        }

        // 另一种启动一个线程的方法，集成runnable并重写run（还是在单个线程里重写）。new ruunable()为new Thread()的参数实际上就是移动一个线程；这里同上也启动了十个线程
        for (int i = 0; i < 10; ++i) {
            // final变量是只读的
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < 10; ++j) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2 %d: %d:", finalI, j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //锁的概念：方法上的锁synchronized (obj)只允许一个线程调用完成才可以给另一个线程调用
    private static Object obj = new Object();

    public static void testSynchronized1() {
        synchronized (obj) {
            try {
                for (int j = 0; j < 10; ++j) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3 %d", j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (new Object()) {
            try {
                for (int j = 0; j < 10; ++j) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4 %d", j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 启动10个线程，每个线程都调用两种方法，由于testSynchronized1（）和testSynchronized2（）锁住的对象不同，所以个线程的调用方法的顺序是乱的，如果锁住的是相同的obj，则调用会一次进行
    public static void testSynchronized() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    // 这个BlockingQueue是什么作用呢：实现一边在插入，一边在取的异步操作
    public static void testBlockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "Consumer1").start();
        new Thread(new Consumer(q), "Consumer2").start();
    }

    // 线程局部变量
    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // threadLocalUserIds实际上包含的是每条线程的ID,get与set方法设置/得到当前线程的ID
                        threadLocalUserIds.set(finalI);
                        Thread.sleep(1000);
                        System.out.println("ThreadLocal:" + threadLocalUserIds.get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        // 这里抛出来的结果全是UserId:9；这是因为第一条启动的线程是i == 9这条线程，而userId是静态变量static int，一旦设置不可更改，否则报错，
        // 相比于上面的ThreadLocal变量threadLocalUserIds对于每个线程的不同的Id,静态的对每个线程的变量符合是不利的
        // finalI是可变的因为它是final的，而static为所有调用所共享，所以不变（userId）,而ThreadLocal变量可以供不同对象调用不同的变量值（即使前面的修饰符是static）（final的变化可以被显现）
        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        userId = finalI;
                        Thread.sleep(1000);
                        System.out.println("UserId:" + userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    // 线程池（execotor）的概念
    public static void testExecutor() {
        //ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        // 很神奇的结构哎。。还可以写run方法。但不用start了
        // 一个线程的submit
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor1:" + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 两个线程并行执行
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 前面提交的的任务执行完关闭，新任务不会被接受
        service.shutdown();
        // 如果任务没有结束，执行shutdown之后
        while (!service.isTerminated()) {
            try {
                Thread.sleep(1000);
                System.out.println("Wait for termination.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 线程的安全性
    // 原始计数器：静态变量计数器
    private static int counter = 0;
    // 原子操作的静态变量
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    // 测试不同线程下的变量的变化
    public static void testWithoutAtomic() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; ++j) {
                            counter++;
                            System.out.println(counter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testWithAtomic() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; ++j) {
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static  void testAtomic() {
        //testWithoutAtomic();
        testWithAtomic();
    }

    // 异步：未来？：返回异步结果、阻塞等待返回结果、超时设置、获取线程中的Exception
    public static void testFuture() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        // 表示不同线程返回的结果是int，Callable类似于Runnable;new Callable<Integer>()表示提交了一组的Thread????
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                throw new IllegalArgumentException("异常");
                // return 1;
            }
        });

        service.shutdown();
        try {
            System.out.println(future.get());
            // 100ms内没完成就报错
            //System.out.println(future.get(100, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] argv) {
        //testThread();
        //testSynchronized();
        // testBlockingQueue();
        // testThreadLocal();
        //testExecutor();
        // testAtomic();
        testFuture();
    }
}
