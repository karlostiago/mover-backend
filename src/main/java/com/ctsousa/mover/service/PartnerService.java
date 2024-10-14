package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.PartnerEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface PartnerService extends BaseService<PartnerEntity, Long> {

    List<PartnerEntity> filterBy(String search);
}
