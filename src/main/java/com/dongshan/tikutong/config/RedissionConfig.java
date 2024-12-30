package com.dongshan.tikutong.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissionConfig {
    private String host;

    private Integer port;

    private Integer database;

//    private String password;

    private Integer timeout;
    @Bean
    public RedissonClient redissonClient() {
        // 如果有密码则填写
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(database)
                .setTimeout(timeout);
//                .setPassword("zndz");
        return Redisson.create(config);
    }
}
