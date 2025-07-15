package com.ctsousa.mover.core;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class CacheEntry<T> {

    private final T value;
    private final long timestamp;

    public CacheEntry(T value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isExpired(Duration maxAge) {
        return Instant.ofEpochMilli(timestamp)
                .plus(maxAge)
                .isBefore(Instant.now());
    }

    public boolean isNotExpired(Duration maxAge) {
        return !isExpired(maxAge);
    }
}
