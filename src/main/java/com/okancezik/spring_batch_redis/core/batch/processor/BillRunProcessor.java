package com.okancezik.spring_batch_redis.core.batch.processor;

import com.okancezik.spring_batch_redis.entity.BillRun;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class BillRunProcessor implements ItemProcessor<BillRun,BillRun> {
	@Override
	public BillRun process(@NonNull BillRun billRun) throws Exception {
		log.info("Processor method has been called. BillRun: {}", billRun);
		billRun.setName(billRun.getName().toUpperCase());
		return billRun;
	}
}
