package com.okancezik.spring_batch_redis.core.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisBaseConfig<K, V> {
	@Bean
	public RedisTemplate<K, V> getRedisTemplate(RedisConnectionFactory redisConnectionFactory, Class<V> type) {
		RedisTemplate<K, V> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(type));
		template.afterPropertiesSet();
		return template;
	}
}
