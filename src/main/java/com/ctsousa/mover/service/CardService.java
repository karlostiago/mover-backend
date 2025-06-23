package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CardService extends BaseService<CardEntity, Long> {

    List<CardEntity> filterBy(String search);

    LocalDate calculateCutOffDate(CardEntity entity, LocalDate date);

    BigDecimal calculateInvoiceValue(CardEntity entity, LocalDate dtInicial, LocalDate dtFinal);
}
