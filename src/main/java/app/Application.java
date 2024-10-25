package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var f = SpringApplication.run(Application.class, args);
        System.out.println(f.getApplicationStartup());
    }
}
