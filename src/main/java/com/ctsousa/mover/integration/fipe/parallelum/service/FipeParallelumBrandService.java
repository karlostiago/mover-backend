package com.ctsousa.mover.integration.fipe.parallelum.service;

import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FipeParallelumBrandService {

    List<FipeParallelumBrandEntity> findAll();
}
