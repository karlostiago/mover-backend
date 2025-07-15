package com.ctsousa.mover.service;

import com.ctsousa.mover.response.ExpectedBalanceResponse;

import java.util.List;

public interface ExpectedBalanceCacheService {

    List<ExpectedBalanceResponse> get(String key);

    void put(String key, List<ExpectedBalanceResponse> value);

    void remove(String key);
}
