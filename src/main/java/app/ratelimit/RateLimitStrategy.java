package app.ratelimit;

public interface RateLimitStrategy {
    boolean isAllowed(String key);
    void consume(String key);
}
