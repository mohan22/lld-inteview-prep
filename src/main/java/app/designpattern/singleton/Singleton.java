package app.designpattern.singleton;

import java.util.concurrent.locks.ReentrantLock;

public class Singleton {
    static Singleton instance;
    static ReentrantLock lock = new ReentrantLock();
    private Singleton(){

    }

    public static Singleton getInstance(){

        lock.lock();
        System.out.println("locked");
        if(instance==null){
            instance = new Singleton();
            System.out.println("initialized singleton");
        }
        lock.unlock();
        System.out.println("unlocked");
        return instance;
    }


    public void costlyMethod(){
        System.out.println("in method");
    }
}
