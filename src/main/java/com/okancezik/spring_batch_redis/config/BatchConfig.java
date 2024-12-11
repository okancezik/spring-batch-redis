package com.okancezik.spring_batch_redis.config;

import com.okancezik.spring_batch_redis.core.batch.reader.BillingItemReader;
import com.okancezik.spring_batch_redis.core.batch.writer.BillingItemWriter;
import com.okancezik.spring_batch_redis.entity.BillRun;
import com.okancezik.spring_batch_redis.core.batch.processor.BillRunProcessor;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.transaction.PlatformTransactionManager;

import static com.okancezik.spring_batch_redis.enums.Keys.HASH_KEY;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
	private final JobRepository              jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public ItemReader<BillRun> reader(RedisTemplate<String, BillRun> redisTemplate) {
		ScanOptions scanOptions = ScanOptions.scanOptions()
				.match(HASH_KEY.getKey() + "*") // Belirli bir deseni eşleştir
				.count(10)      // Her seferde 10 anahtar getir
				.build();
		return new BillingItemReader<String, BillRun>()
				.reader(redisTemplate, scanOptions);
	}

	@Bean
	public BillRunProcessor processor() {
		return new BillRunProcessor();
	}

	@Bean
	public ItemWriter<BillRun> writer(RedisTemplate<String, BillRun> redisTemplate) {
		// itemKeyMapper ile her öğe için anahtar belirliyoruz
		Converter<BillRun, String> itemKeyMapper = item -> {
			log.info("Writer convert, item: {}", item);
			return HASH_KEY.getKey() + item.getId();
		};
		return new BillingItemWriter<String, BillRun>()
				.writer(redisTemplate, itemKeyMapper);
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


