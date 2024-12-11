package com.okancezik.spring_batch_redis.enums;

import lombok.Getter;

@Getter
public enum Keys {
	HASH_KEY("BillRuns::");
	private final String key;

	Keys(String key) {
		this.key = key;
	}
}
