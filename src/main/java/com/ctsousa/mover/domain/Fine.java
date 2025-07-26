package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Fine extends DomainModel<FineEntity> {
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
    private String signature;
    private BigDecimal discount;

    @Override
    public FineEntity toEntity() {
        FineEntity entity = new FineEntity();
        entity.setId(this.getId());
        entity.setInfractionNotice(infractionNotice);
        entity.setNumberRenainf(numberRenainf);
        entity.setInfractionCode(infractionCode);
        entity.setDateTimeOfCommitment(dateTimeOfCommitment);
        entity.setDueDate(dueDate);
        entity.setExpirationInfraction(expirationInfraction);
        entity.setClient(client != null ? new ClientEntity(client.getId()) : null);
        entity.setVehicle(vehicle != null ? new VehicleEntity(vehicle.getId()) : null);
        entity.setValue(value);
        entity.setOriginalValue(originalValue);
        entity.setDescription(description);
        entity.setRealOffender(realOffender);
        entity.setAccount(account != null ? new AccountEntity(account.getId()) : null);
        entity.setCard(card != null ? new CardEntity(card.getId()) : null);
        entity.setDiscount(this.discount != null ? this.discount : BigDecimal.ZERO);
        return entity;
    }
}
