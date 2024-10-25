package app.ratelimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class RateLimitController {

    @Autowired
    RateLimitService rateLimitService;

    @GetMapping("/test")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello world");
    }
    @GetMapping("/isallowed")
    public ResponseEntity<Boolean> isAllowed(@RequestParam String service, @RequestParam String key){
        if(rateLimitService.isAllowed(service,key)){
            return ResponseEntity.ok(true);
        }
        else{
           return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    @GetMapping("/consume")
    public ResponseEntity<Boolean> consume(@RequestParam String service, @RequestParam String key){
        rateLimitService.consume(service,key);
        return ResponseEntity.ok(true);
    }
}
