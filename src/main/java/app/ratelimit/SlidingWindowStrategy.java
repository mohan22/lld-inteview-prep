package app.ratelimit;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SlidingWindowStrategy implements RateLimitStrategy{
    Deque<LocalDateTime> concurrentLinkedDeque;
    public SlidingWindowStrategy(){
        concurrentLinkedDeque = new ConcurrentLinkedDeque<>();
    }
    @Override
    public boolean isAllowed(String key) {
       LocalDateTime localDateTime = LocalDateTime.now();

       while(!concurrentLinkedDeque.isEmpty()&&ChronoUnit.SECONDS.between(concurrentLinkedDeque.peekFirst(),localDateTime) > 60)
           concurrentLinkedDeque.removeFirst();
        System.out.println("window size :: " + concurrentLinkedDeque.size());
        return concurrentLinkedDeque.size() < 5;

    }

    @Override
    public void consume(String key) {
        concurrentLinkedDeque.addLast(LocalDateTime.now());
        System.out.println("window size after consumption:: " + concurrentLinkedDeque.size());
    }
}
