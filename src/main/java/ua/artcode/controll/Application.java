package ua.artcode.controll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Алексей on 17.04.2017.
 */
@SpringBootApplication(scanBasePackages = {"ua.artcode"})
@PropertySource("classpath:app.properties")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
