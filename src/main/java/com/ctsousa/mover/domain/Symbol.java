package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class Symbol implements MapperToEntity<SymbolEntity> {

    private Long id;

    private String description;

    private String imageBase64;

    public Symbol(String description, String imageBase64) {
        setDescription(description);
        setImageBase64(imageBase64);
    }

    public void setDescription(String description) {
        if (StringUtils.isBlank(description)) throw new NotificationException("");
        if (StringUtils.equalsIgnoreCase(description, "undefined")) throw new NotificationException("");
        this.description = description.replace(",", "")
                .trim().toUpperCase();
    }

    public void setImageBase64(String imageBase64) {
        if (StringUtils.isBlank(imageBase64)) throw new NotificationException("");
        if (StringUtils.equalsIgnoreCase(imageBase64, "undefined")) throw new NotificationException("");
        this.imageBase64 = imageBase64;
    }

    @Override
    public SymbolEntity toEntity() {
        SymbolEntity entity = new SymbolEntity();
        entity.setId(this.getId());
        entity.setDescription(this.getDescription().toUpperCase());
        entity.setImageBase64(this.getImageBase64());
        entity.setActive(true);
        return entity;
    }
}
