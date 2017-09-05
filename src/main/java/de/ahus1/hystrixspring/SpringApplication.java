package de.ahus1.hystrixspring;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Alexander Schwartz 2017
 */
@SpringBootApplication
public class SpringApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(SpringApplication.class)
                .run(args);
    }
}
