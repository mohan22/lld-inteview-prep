package app.multithreading;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EvenOddPrinter {
    static int n;
    static Object lock = new Object();

    public static void main(String[] args) {
//        usingAtomicInt();
//        usingSynchronization();
        usingReentrantLock();
    }

    static void usingReentrantLock() {
        Lock lock = new ReentrantLock();
        Condition evenCond = lock.newCondition();
        Condition oddCond = lock.newCondition();
        n = 0;
        Thread t1 = new Thread(() -> {
            while (n < 10) {
                lock.lock();
                try {
                    if (n % 2 != 0) {
                        evenCond.await();
                    }

                    System.out.println("Thread name " + Thread.currentThread().getName() + " " + n);
                    n++;
                    oddCond.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });


        Thread t2 = new Thread(() -> {
            while (n < 10) {
                lock.lock();
                try {
                    if (n % 2 != 1) {
                        oddCond.await();
                    }
                    System.out.println("Thread name " + Thread.currentThread().getName() + " " + n);
                    n++;
                    evenCond.signal();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }

            }
        });

        t1.start();
        t2.start();
    }

    static void usingAtomicInt() {
        AtomicInteger num = new AtomicInteger(0);

        Runnable r1 = () -> {
            while (num.get() < 10) {
                if (num.get() % 2 == 0) {
                    System.out.println("Thread name " + Thread.currentThread().getName() + " " + num.get());
                    num.getAndAdd(1);
                }
            }
        };

        Runnable r2 = () -> {
            while (num.get() < 10) {
                if (num.get() % 2 == 1) {
                    System.out.println("Thread name " + Thread.currentThread().getName() + " " + num.get());
                    num.getAndAdd(1);
                }
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }

    static void usingSynchronization() {
        n = 0;
        Thread t1 = new Thread(() -> {
            while (n < 10) {
                synchronized (lock) {
                    if (n % 2 == 0) {
                        System.out.println("Thread name " + Thread.currentThread().getName() + " " + n);
                        n++;
                        lock.notify();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });


        Thread t2 = new Thread(() -> {
            while (n < 10) {
                synchronized (lock) {
                    if (n % 2 == 1) {
                        System.out.println("Thread name " + Thread.currentThread().getName() + " " + n);
                        n++;
                        lock.notify();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        t1.start();
        t2.start();

    }
}
