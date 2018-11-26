package io.oreon.tapad.analytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class RedisConfig {

    @Bean
    public EmbeddedRedis embeddedRedis() {
        return new EmbeddedRedis();
    }

    private class EmbeddedRedis {

        private RedisServer redisServer;

        @Value("${embedded.redis.port}")
        private int port;

        @PostConstruct
        public void start() throws IOException {
            this.redisServer = new RedisServer(this.port);
            this.redisServer.start();
        }

        @PreDestroy
        public void stop() {
            this.redisServer.stop();
        }
    }

}
