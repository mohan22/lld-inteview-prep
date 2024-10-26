package app.designpattern.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args){

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for(int i=0;i<50;i++) {
            executorService.submit(() -> {
//                Singleton singleton = Singleton.getInstance();
                SingletonUsingEnum singleton = SingletonUsingEnum.INSTANCE;
                singleton.costlyMethod();
            });
        }

        executorService.shutdown();

        //executorService.awaitTermination();

    }
}
