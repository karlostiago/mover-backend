package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.time.LocalDate;
import java.util.List;

public interface CardService extends BaseService<CardEntity, Long> {

    List<CardEntity> filterBy(String search);

    LocalDate calculateCutOffDate(CardEntity entity);
}
