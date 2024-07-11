package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.mapper.MapperToDomain;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import com.ctsousa.mover.domain.Sender;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SenderMapper implements MapperToEntity<SenderEntity, Sender>, MapperToDomain<Sender, SenderEntity> {
    @Override
    public Sender toDomain(SenderEntity entity) {
        Sender sender = new Sender();
        sender.setCode(entity.getCode());
        sender.setEmail(entity.getEmail());
        sender.setExpiryDate(entity.getExpiryDate());
        sender.setClientId(entity.getClientId());
        sender.setId(entity.getId());
        return sender;
    }

    @Override
    public List<Sender> toDomains(List<SenderEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public SenderEntity toEntity(Sender domain) {
        SenderEntity senderEntity = new SenderEntity();
        senderEntity.setCode(domain.getCode());
        senderEntity.setEmail(domain.getEmail());
        senderEntity.setExpiryDate(domain.getExpiryDate());
        senderEntity.setClientId(domain.getClientId());
        senderEntity.setId(domain.getId());
        return senderEntity;
    }

    @Override
    public List<SenderEntity> toEntities(List<Sender> domains) {
        return domains.stream()
                .map(this::toEntity)
                .toList();
    }
}
