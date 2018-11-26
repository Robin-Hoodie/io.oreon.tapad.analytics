package io.oreon.tapad.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TapadAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TapadAnalyticsApplication.class, args);
    }
}
