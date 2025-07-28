package app.multithreading;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeMathOps {
    static volatile int min = 1000;
    static Integer mode;
    static int modeFreq=0;
    static Map<Integer, Integer> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        //minimumCalculation();
        modeCalculation();

    }

    static void modeCalculation() {
        ExecutorService es = Executors.newFixedThreadPool(2);
        Lock lock = new ReentrantLock();
        Random rand = new Random();
        es.submit(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    int n = rand.nextInt() % (i + 1);
                    System.out.println("adding number :: " + n);
                    map.putIfAbsent(n, 0);
                    map.put(n, map.get(n) + 1);
                    if (mode == null || map.get(n) > modeFreq) {
                        mode = n;
                        modeFreq = map.get(n);
                    }
                }finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        es.submit(()->{
            for(int i=0;i<10;i++){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("mode :: "+mode + " freq: "+modeFreq);
            }

        });

        es.shutdown();
    }

    static void minimumCalculation() {
        try (ExecutorService es = Executors.newFixedThreadPool(2)) {
            Lock lock = new ReentrantLock();
            //Random random = new Random();
            //AtomicInteger num = new AtomicInteger();
            es.submit(() -> {
                for (int i = 0; i < 10; i++) {

                    lock.lock();
                    try {
                        //int cur = num.decrementAndGet();
                        min = Math.min(min, 100 - i);
                        System.out.println(new Date() + " updated to  :: " + min);
                    } finally {
                        lock.unlock();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            });


            es.submit(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println(new Date() + " current min :: " + min);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            es.shutdown();
        }
    }


}
