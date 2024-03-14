package edu.java;

import edu.java.configuration.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApplicationConfig.class)
@EnableTransactionManagement
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
