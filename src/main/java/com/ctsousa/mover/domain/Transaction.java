package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.core.util.HashUtil;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.UUID;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static java.util.UUID.randomUUID;

@Getter
@Setter
public class Transaction extends DomainModel<TransactionEntity> {

    private Long id;
    private String description;
    private SubCategory subcategory;
    private Integer installment;
    private String paymentType;
    private String frequency;
    private String categoryType;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private BigDecimal value;
    private Card card;
    private Account account;
    private Account destinationAccount;
    private Vehicle vehicle;
    private Contract contract;
    private Partner partner;
    private String signature;
    private String transactionType;
    private Boolean paid;
    private Boolean predicted;

    @Override
    public TransactionEntity toEntity() {
        TransactionEntity entity = new TransactionEntity();

        entity.setId(this.getId());
        entity.setDescription(toUppercase(this.getDescription()));
        entity.setSubcategory(this.getSubcategory().toEntity());
        entity.setInstallment(this.getInstallment());
        entity.setPaymentType(this.getPaymentType());
        entity.setFrequency(this.getFrequency());

        TypeCategory typeCategory = TypeCategory.toDescription(this.getCategoryType());
        entity.setCategoryType(typeCategory.name());

        entity.setTransactionType(TransactionType.DEBIT.name());

        entity.setDueDate(this.getDueDate());
        entity.setPaymentDate(this.getPaymentDate());
        entity.setValue(this.getValue());
        entity.setAccount(new AccountEntity(this.getAccount().getId()));

        if (this.getCard() != null && (this.getCard().getId() != null && this.getCard().getId() > 0)) {
            entity.setCard(new CardEntity(this.getCard().getId()));
        }
        if (this.getVehicle() != null && (this.getVehicle().getId() != null && this.getVehicle().getId() > 0)) {
            entity.setVehicle(new VehicleEntity(this.getVehicle().getId()));
        }
        if (this.getContract() != null && (this.getContract().getId() != null && this.getContract().getId() > 0)) {
            entity.setContract(new ContractEntity(this.getContract().getId()));
        }
        if (this.getPartner() != null && (this.getPartner().getId() != null && this.getPartner().getId() > 0)) {
            entity.setPartner(new PartnerEntity(this.getPartner().getId()));
        }

        entity.setPaid(this.getPaid());
        entity.setActive(this.getActive());
        entity.setPredicted(Boolean.FALSE);
        entity.setRefund(Boolean.FALSE);

        if (entity.getId() == null || entity.getId() == 0) {
            entity.setSignature(String.valueOf(randomUUID()));
        }

        entity.setHour(LocalTime.now());

        return entity;
    }
}
