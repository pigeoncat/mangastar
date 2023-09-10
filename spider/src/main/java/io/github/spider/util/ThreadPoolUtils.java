package io.github.spider.util;

import org.springframework.core.task.TaskRejectedException;

import java.util.concurrent.*;

/**
 * @Author pigeoncat
 * @Date 2023/08/29  12:22
 * @TODO description
 */
public class ThreadPoolUtils {

    private static ExecutorService threadPool;

    static {
        //系统线程数
//        int systemThreads = 2;
        int systemThreads = Runtime.getRuntime().availableProcessors();
        int queueCapacity=200;
        threadPool=new ThreadPoolExecutor(
                systemThreads,
                systemThreads,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueCapacity),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        throw new TaskRejectedException("阻塞队列已满，线程池忙碌...");
                    }
                }
        );

    }

    public static void main(String[] args) {
        ExecutorService threadPool = ThreadPoolUtils.getThreadPool();
        for (int i = 0; i < 200; i++) {
            int finalI = i;
            try {
                ThreadPoolUtils.threadPool.submit(()->{
                    System.out.println("任务"+ finalI);
                });
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("先睡眠3秒");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static  ExecutorService  getThreadPool(){
        return threadPool;
    }


    private ThreadPoolUtils() {
    }
}
