package com.okancezik.spring_batch_redis.core.batch.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.redis.builder.RedisItemWriterBuilder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;

public class BillingItemWriter<K, V> {
	public ItemWriter<V> writer(RedisTemplate<K, V> redisTemplate, Converter<V, K> itemKeyMapper) {
		return new RedisItemWriterBuilder<K, V>()
				.redisTemplate(redisTemplate)
				.itemKeyMapper(itemKeyMapper)
				.delete(false)
				.build();
	}
}
