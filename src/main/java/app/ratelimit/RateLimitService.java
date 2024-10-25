package app.ratelimit;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitService {
    Map<String, RateLimitConfig> configMap = new HashMap<>();

    public RateLimitService(){
        System.out.println("RateLimitService init");
        RateLimitConfig rateLimitConfig = new RateLimitConfig();
        rateLimitConfig.strategy = new TokenBucketStrategy();
        configMap.put("abc",rateLimitConfig);
    }
    public boolean isAllowed(String service, String key){
        return configMap.get(service).strategy.isAllowed(key);
    }

    public void consume(String service,String key){
        configMap.get(service).strategy.consume(key);
    }

}
