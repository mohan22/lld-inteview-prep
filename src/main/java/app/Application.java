package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var f = SpringApplication.run(Application.class, args);
        System.out.println(f.getApplicationStartup());
        Map<Integer,Integer> map = new HashMap<>();
        map.put(1,1);
    }
}
