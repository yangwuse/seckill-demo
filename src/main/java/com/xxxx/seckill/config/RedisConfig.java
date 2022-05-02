package com.xxxx.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类，实现序列化
 * @author yangWu
 * @date 2022/4/7 10:41 PM
 * @version 1.0
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // key 序列化
        template.setKeySerializer(new StringRedisSerializer());
        // value 序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash key 序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        // hash value 序列化
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());


        // 注入连接工厂
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public DefaultRedisScript<Long> script() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        // lock.lua 脚本位置和 application.yml 在同一级目录
        script.setLocation(new ClassPathResource("stock.lua"));
        script.setResultType(Long.class);
        return script;
    }
}


