package app.ratelimit;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucket {
    AtomicInteger tokens;
    LocalDateTime lastRefilTime;
    int refilRate;
    int maxTokens;

    int refilTime;

    public TokenBucket(int maxTokens, int refilRate, int refilTime){
        System.out.println("TokenBucket init");
        tokens = new AtomicInteger();
        tokens.set(maxTokens);
        this.maxTokens = maxTokens;
        this.refilRate = refilRate;
        this.refilTime = refilTime;
        lastRefilTime = LocalDateTime.now();
    }

    public synchronized boolean hasTokens(){
        refill();
        System.out.println("available tokens :: " + tokens);
        return tokens.get()>0;
    }

    void refill(){
        int secs = (int)ChronoUnit.SECONDS.between(lastRefilTime,LocalDateTime.now());
        System.out.println("secs :: " + secs);
        int refilTokens = (secs/refilTime)*refilRate;
        if(refilTokens>0) {
            tokens.addAndGet(Math.min(refilTokens,maxTokens-tokens.get()));
            lastRefilTime = LocalDateTime.now();
        }

    }

    synchronized void consume(){
        if(tokens.get()>0)
            tokens.decrementAndGet();

        System.out.println("remaing tokens :: " + tokens);
    }
}
