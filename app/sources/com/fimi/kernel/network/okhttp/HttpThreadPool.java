package com.fimi.kernel.network.okhttp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpThreadPool {
    private static int CORE_POOL_SIZE = 5;
    private static int KEEP_ALIVE_TIME = 10000;
    private static int MAX_POOL_SIZE = 100;
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        public Thread newThread(Runnable r) {
            return new Thread(r, "WL-Thread #" + this.integer.getAndIncrement());
        }
    };
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, (long) KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(10);

    private HttpThreadPool() {
    }

    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPool;
    }
}
