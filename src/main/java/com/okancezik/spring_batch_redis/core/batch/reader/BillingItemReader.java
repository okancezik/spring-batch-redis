package com.okancezik.spring_batch_redis.core.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.redis.builder.RedisItemReaderBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

public class BillingItemReader<K, V> {
	public ItemReader<V> reader(RedisTemplate<K, V> redisTemplate, ScanOptions scanOptions) {
		return new RedisItemReaderBuilder<K, V>()
				.redisTemplate(redisTemplate)
				.scanOptions(scanOptions)
				.build();
	}
}
