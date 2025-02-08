package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ProfileEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface ProfileService extends BaseService<ProfileEntity, Long> {

    List<ProfileEntity> filterBy(String search);
}
