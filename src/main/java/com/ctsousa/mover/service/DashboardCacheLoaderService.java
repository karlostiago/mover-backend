package com.ctsousa.mover.service;

import com.ctsousa.mover.domain.DashboardCache;

import java.time.LocalDate;

public interface DashboardCacheLoaderService {

    DashboardCache load(LocalDate dtInitial, LocalDate dtFinal);
}
