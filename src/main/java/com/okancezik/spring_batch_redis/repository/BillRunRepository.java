package com.okancezik.spring_batch_redis.repository;

import com.okancezik.spring_batch_redis.entity.BillRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRunRepository extends JpaRepository<BillRun, Long> {
}
