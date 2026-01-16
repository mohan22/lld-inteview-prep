package app.multithreading;

import java.util.Date;
import java.util.concurrent.*;

public class ThreadPoolExample {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = new ThreadPoolExecutor(0,30,10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1));//Executors.//Executors.newFixedThreadPool(5);
        //service = Executors.newFixedThreadPool(10);
        int start = 1;
        int end = 20;
        System.out.println(new Date());
        for(int i=start;i<=end;i++){
            int finalI = i;
            service.submit(()-> {
                try {
                    print(finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }


        service.shutdown();
        System.out.println(service.awaitTermination(1,TimeUnit.MINUTES));
        System.out.println(new Date());


    }

    static void print(int n) throws InterruptedException {
        Thread.sleep(5000);
        System.out.println(new Date() +" :: " +Thread.currentThread().getName() + " :: " + n);
    }


}
