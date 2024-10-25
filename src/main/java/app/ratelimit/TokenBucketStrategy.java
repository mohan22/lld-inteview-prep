package app.ratelimit;

import java.util.HashMap;
import java.util.Map;

public class TokenBucketStrategy implements RateLimitStrategy{

    Map<String,TokenBucket> map = new HashMap<>();

    public TokenBucketStrategy(){
        TokenBucket bucket = new TokenBucket(5,1,60);
        map.put("123",bucket);
    }

    @Override
    public boolean isAllowed(String key) {
        return map.get(key).hasTokens();
    }

    @Override
    public void consume(String key) {
        map.get(key).consume();
    }
}
