package com.okancezik.spring_batch_redis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BillRun {
	@Id
	private long id;
	private String name;
	private LocalDateTime billDate;
	private Long billingAccountId;
}
