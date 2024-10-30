package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.MaintenanceEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Maintenance extends DomainModel<MaintenanceEntity> {

    private Vehicle vehicle;
    private Account account;
    private Card card;
    private LocalDate date;
    private Long mileage;
    private String establishment;
    private String type;
    private String detail;
    private BigDecimal value;

    @Override
    public MaintenanceEntity toEntity() {
        MaintenanceEntity entity = new MaintenanceEntity();
        entity.setAccount(new AccountEntity(account.getId()));
        entity.setVehicle(new VehicleEntity(vehicle.getId()));

        if (card != null && card.getId() != null) {
            entity.setCard(new CardEntity(card.getId()));
        }

        entity.setDate(this.getDate());
        entity.setMileage(this.getMileage());
        entity.setEstablishment(toUppercase(this.getEstablishment()));
        entity.setDetail(toUppercase(this.getDetail()));
        entity.setType(toUppercase(this.getType()));
        entity.setValue(this.getValue());
        entity.setActive(this.getActive());

        return entity;
    }
}
