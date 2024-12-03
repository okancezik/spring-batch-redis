package com.okancezik.spring_batch_redis.processors;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class BillRunProcessor implements ItemProcessor<String,String> {
	@Override
	public String process(@NonNull String billRun) throws Exception {
		log.info("Processor method has been called. BillRun: {}", billRun);
		billRun = billRun.toUpperCase();
		return billRun;
	}
}
