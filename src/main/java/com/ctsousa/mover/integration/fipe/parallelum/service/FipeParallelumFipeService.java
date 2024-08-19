package com.ctsousa.mover.integration.fipe.parallelum.service;

import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumFipeEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumYearEntity;
import org.springframework.stereotype.Service;

@Service
public interface FipeParallelumFipeService {

    FipeParallelumFipeEntity findBy(FipeParallelumBrandEntity brandEntity, FipeParallelumModelEntity modelEntity, FipeParallelumYearEntity yearEntity);
}
