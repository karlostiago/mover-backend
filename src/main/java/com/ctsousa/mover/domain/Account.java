package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Getter
@Setter
public class Account extends DomainModel<AccountEntity> {

    private String name;
    private String icon;
    private BigDecimal InitialBalance;
    private Boolean caution;

    @Override
    public AccountEntity toEntity() {
        AccountEntity entity = new AccountEntity();
        entity.setId(this.getId());
        entity.setCaution(this.getCaution());
        entity.setActive(this.getActive());
        entity.setName(this.getName());
        entity.setIcon(this.getIcon());
        entity.setInitialBalance(this.getInitialBalance());
        return entity;
    }
}
