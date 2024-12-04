package com.okancezik.spring_batch_redis.config;

import com.okancezik.spring_batch_redis.entity.BillRun;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
		RedisTemplate<String, BillRun> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		/*
		 * KEY,VALUE SERIALIZER SETTINGS
		 * */
		template.setKeySerializer(new StringRedisSerializer());
		//template.setValueSerializer(new StringRedisSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(BillRun.class));
		template.afterPropertiesSet();
		return template;
	}
}
