package com.ctsousa.mover.integration.fipe.parallelum.service;

import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumYearEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FipeParallelumYearService {

    List<FipeParallelumYearEntity> findBy(FipeParallelumBrandEntity brandEntity, FipeParallelumModelEntity modelEntity);
}
