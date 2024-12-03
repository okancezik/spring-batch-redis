package com.okancezik.spring_batch_redis.api;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bill-run")
public class BillRunApi {
	private final Job         job;
	private final JobLauncher jobLauncher;

	@GetMapping
	public void startJob() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(job, jobParameters);
		} catch (
				JobExecutionAlreadyRunningException
						| JobRestartException
						| JobInstanceAlreadyCompleteException
						| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}
}
