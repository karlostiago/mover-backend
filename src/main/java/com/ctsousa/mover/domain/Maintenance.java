package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.MaintenanceEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.enumeration.TypeMaintenance;
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
        entity.setId(this.getId());
        entity.setAccount(new AccountEntity(account.getId()));
        entity.setVehicle(new VehicleEntity(vehicle.getId()));

        if (card != null && card.getId() != null) {
            entity.setCard(new CardEntity(card.getId()));
        }

        entity.setDate(this.getDate());
        entity.setMileage(this.getMileage());
        entity.setEstablishment(toUppercase(this.getEstablishment()));
        entity.setDetail(toUppercase(this.getDetail()));

        TypeMaintenance type = TypeMaintenance.toDescription(toUppercase(this.getType()));
        entity.setType(type.name());

        entity.setValue(this.getValue());

        if (entity.getValue() == null || entity.getValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new NotificationException("Valor de manutenção não pode ser zero.");
        }

        entity.setActive(this.getActive());

        return entity;
    }
}
