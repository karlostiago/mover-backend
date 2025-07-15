package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.CacheEntry;
import com.ctsousa.mover.response.ExpectedBalanceResponse;
import com.ctsousa.mover.service.ExpectedBalanceCacheService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExpectedBalanceCacheServiceImpl implements ExpectedBalanceCacheService {

    private static final Map<String, CacheEntry<List<ExpectedBalanceResponse>>> expectedBalanceCache = new ConcurrentHashMap<>();
    private final Duration CACHE_TTL = Duration.ofMinutes(5); // 5 minutos

    @Override
    public List<ExpectedBalanceResponse> get(String key) {
        CacheEntry<List<ExpectedBalanceResponse>> cacheEntry = expectedBalanceCache.get(key);
        if (cacheEntry != null && cacheEntry.isNotExpired(CACHE_TTL)) {
            return cacheEntry.getValue();
        } else {
            remove(key);
            return null;
        }
    }

    @Override
    public void put(String key, List<ExpectedBalanceResponse> value) {
        expectedBalanceCache.put(key, new CacheEntry<>(value));
    }

    @Override
    public void remove(String key) {
        expectedBalanceCache.remove(key);
    }

    @Scheduled(fixedRate = 120000) // 2 minutos
    public void evictExpiredEntries() {
        expectedBalanceCache.entrySet().removeIf(entry -> entry.getValue().isExpired(CACHE_TTL));
    }
}
