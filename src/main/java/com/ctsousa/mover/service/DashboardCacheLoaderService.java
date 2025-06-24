package com.ctsousa.mover.service;

import com.ctsousa.mover.domain.DashboardCache;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public interface DashboardCacheLoaderService {

    CompletableFuture<DashboardCache> loadAsync(LocalDate dtInitial, LocalDate dtFinal);
    DashboardCache load(LocalDate dtInitial, LocalDate dtFinal);
}
