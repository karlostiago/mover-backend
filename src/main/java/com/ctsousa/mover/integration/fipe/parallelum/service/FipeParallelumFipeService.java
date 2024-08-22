package com.ctsousa.mover.integration.fipe.parallelum.service;

import com.ctsousa.mover.integration.fipe.parallelum.entity.*;
import org.springframework.stereotype.Service;

@Service
public interface FipeParallelumFipeService {

    FipeParallelumFipeEntity findBy(FipeParallelumBrandEntity brandEntity, FipeParallelumModelEntity modelEntity, FipeParallelumYearEntity yearEntity, FipeParallelumReferenceEntity referenceEntity);
}
