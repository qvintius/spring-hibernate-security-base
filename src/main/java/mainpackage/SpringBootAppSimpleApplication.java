package mainpackage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootAppSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAppSimpleApplication.class, args);
        System.out.println("Hello World!");
    }

}
