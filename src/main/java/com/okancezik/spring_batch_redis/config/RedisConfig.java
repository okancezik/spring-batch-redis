package com.okancezik.spring_batch_redis.config;

import com.okancezik.spring_batch_redis.core.redis.RedisBaseConfig;
import com.okancezik.spring_batch_redis.entity.BillRun;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(new RedisStandaloneConfiguration("127.0.0.1", 6379));
		connectionFactory.start();
		return connectionFactory;
	}

	@Bean
	public RedisTemplate<String, BillRun> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisBaseConfig<String, BillRun> baseConfig = new RedisBaseConfig<>();
		return baseConfig.getRedisTemplate(redisConnectionFactory, BillRun.class);
	}
}
