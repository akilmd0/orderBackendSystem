package com.example.ecommerce.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.protocol.ProtocolVersion;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisClient redisClient() {
        RedisURI redisUri = RedisURI.builder()
                .withHost("localhost")
                .withPort(6379)
                .withPassword("1234abcd")  // can make this configurable by using from env leting it constant for now
                .build();

        RedisClient redisClient = RedisClient.create(redisUri);
        redisClient.setOptions(ClientOptions.builder()
                .protocolVersion(ProtocolVersion.RESP2) // Force RESP2 to avoid HELLO auth issue
                .build());

        return redisClient;
    }

    @Bean
    public StatefulRedisConnection<String, String> redisConnection(RedisClient redisClient) {
        return redisClient.connect();
    }
}
