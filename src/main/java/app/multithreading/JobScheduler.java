package app.multithreading;

import org.apache.catalina.filters.RateLimitFilter;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;


class Job{
    int id;
    int priority;

    Job(int id, int priority){
        this.id = id;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

class JobQ{
    PriorityBlockingQueue<Job> jobQ;

    JobQ(){
        jobQ = new PriorityBlockingQueue<>(10,Comparator.comparingInt(Job::getPriority).reversed());
    }

    Job poll(){
        return jobQ.poll();
    }

    void add(Job job){
        jobQ.add(job);
    }
}

class Worker implements Runnable{
    JobQ jobQ;
    int id=-1;

    RateLimiter rateLimiter;



    public Worker(JobQ q){
        jobQ = q;
    }

    public Worker(JobQ q,int id){
        jobQ = q;
        this.id = id;
    }

    public Worker(JobQ q,int id, double jobsPerSec){
        this(q,id);
        rateLimiter = RateLimiter.create(jobsPerSec);
    }



    public void process() throws InterruptedException {
        while(true){
            Job job = jobQ.poll();
            if(job==null)
                continue;
            System.out.println("Processing job :: " + job.id + " by the thread :: " + Thread.currentThread().getName() + " worker id :: " + id);
            Thread.sleep(5000);
        }
    }

    @Override
    public void run() {
        while (true) {
            rateLimiter.acquire();
            Job job = jobQ.poll();
            if (job == null)
                continue;
            System.out.println("Processing job :: " + job.id + " by the thread :: " + Thread.currentThread().getName() + " worker id :: " + id);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class JobScheduler {
    public static void main(String[] args) throws InterruptedException {

        JobQ jobQ = new JobQ();
        jobQ.add(new Job(1,1));
        jobQ.add(new Job(2,2));
        jobQ.add(new Job(3,3));
        jobQ.add(new Job(4,4));
        jobQ.add(new Job(5,5));
        Worker worker = new Worker(jobQ);

        //worker.process();


        ExecutorService es = Executors.newFixedThreadPool(3);
        es.submit(new Worker(jobQ,1, 3.0));
        for(int i=1;i<3;i++){
            es.submit(new Worker(jobQ,i+1,0.05));
        }

        es.shutdown();
        es.awaitTermination(20000, TimeUnit.MILLISECONDS);


    }
}
