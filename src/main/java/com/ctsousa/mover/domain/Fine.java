package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Fine implements MapperToEntity<FineEntity> {
    private String infractionNotice;
    private BigInteger numberRenainf;
    private BigInteger infractionCode;
    private LocalDateTime dateTimeOfCommitment;
    private LocalDate dueDate;
    private LocalDate expirationInfraction;
    private Client client;
    private Vehicle vehicle;
    private BigDecimal value;
    private BigDecimal originalValue;
    private String description;
    private Boolean realOffender;
    private Account account;
    private Card card;

    @Override
    public FineEntity toEntity() {
        FineEntity entity = new FineEntity();
        entity.setInfractionNotice(infractionNotice);
        entity.setNumberRenainf(numberRenainf);
        entity.setInfractionCode(infractionCode);
        entity.setDateTimeOfCommitment(dateTimeOfCommitment);
        entity.setDueDate(dueDate);
        entity.setExpirationInfraction(expirationInfraction);
        entity.setClient(client != null ? client.toEntity() : null);
        entity.setVehicle(vehicle != null ? vehicle.toEntity() : null);
        entity.setValue(value);
        entity.setOriginalValue(originalValue);
        entity.setDescription(description);
        entity.setRealOffender(realOffender);
        entity.setAccount(account != null ? account.toEntity() : null);
        entity.setCard(card != null ? card.toEntity() : null);
        return entity;
    }
}
