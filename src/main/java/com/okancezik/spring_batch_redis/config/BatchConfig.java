package com.okancezik.spring_batch_redis.config;

import com.okancezik.spring_batch_redis.entity.BillRun;
import com.okancezik.spring_batch_redis.processors.BillRunProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.redis.builder.RedisItemReaderBuilder;
import org.springframework.batch.item.redis.builder.RedisItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
	private final        JobRepository              jobRepository;
	private final        PlatformTransactionManager transactionManager;
	private final static String                     HASH_KEY = "billRuns::";

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

	@Bean
	public ItemWriter<BillRun> writer(RedisTemplate<String, BillRun> redisTemplate) {
		// itemKeyMapper ile her öğe için anahtar belirliyoruz
		Converter<BillRun, String> itemKeyMapper = item -> {
			log.info("Writer convert, item: {}", item);
			// Verilen item, anahtar olarak kullanılacak
			return HASH_KEY + item.getId();
		};
		return new RedisItemWriterBuilder<String, BillRun>()
				.redisTemplate(redisTemplate)
				.itemKeyMapper(itemKeyMapper)
				.delete(false)  // Veriyi silmiyoruz, güncelleme yapacağız
				.build();
	}

	@Bean
	public ItemReader<BillRun> reader(RedisTemplate<String, BillRun> redisTemplate) {
		ScanOptions scanOptions = ScanOptions.scanOptions()
				.match("billRuns::*") // Belirli bir deseni eşleştir
				.count(1)      // Her seferde 10 anahtar getir
				.build();
		return new RedisItemReaderBuilder<String, BillRun>()
				.redisTemplate(redisTemplate)
				.scanOptions(scanOptions)
				.build();
	}

	@Bean
	public BillRunProcessor processor() {
		return new BillRunProcessor();
	}

	@Bean
	public Step step(ItemReader<BillRun> reader, ItemWriter<BillRun> writer, ItemProcessor<BillRun, BillRun> processor) {
		return new StepBuilder("spring-batch-redis-step", jobRepository)
				.<BillRun, BillRun> chunk(1, transactionManager)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				//.writer(items -> log.info(items.toString()))
				.build();
	}

	@Bean
	public Job job(Step step) {
		return new JobBuilder("redis", jobRepository)
				.start(step)
				.build();
	}
}


