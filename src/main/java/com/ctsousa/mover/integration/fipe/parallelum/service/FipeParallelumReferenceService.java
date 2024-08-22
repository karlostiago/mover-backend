package com.ctsousa.mover.integration.fipe.parallelum.service;

import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumReferenceEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FipeParallelumReferenceService {

    List<FipeParallelumReferenceEntity> findAll();
}
