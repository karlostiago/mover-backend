package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.CardApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Card;
import com.ctsousa.mover.enumeration.BankIcon;
import com.ctsousa.mover.request.CardRequest;
import com.ctsousa.mover.response.BankIconResponse;
import com.ctsousa.mover.response.CardResponse;
import com.ctsousa.mover.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/cards")
public class CardResource extends BaseResource<CardResponse, CardRequest, CardEntity> implements CardApi {

    @Autowired
    private CardService cardService;

    public CardResource(CardService cardService) {
        super(cardService);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Card.REGISTER_CARDS)
    public ResponseEntity<CardResponse> add(CardRequest request) {
        Card card = toMapper(request, Card.class);
        CardEntity entity = cardService.save(card.toEntity());
        return ResponseEntity.ok(toMapper(entity, CardResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Card.UPDATE_CARDS)
    public ResponseEntity<CardResponse> update(Long id, CardRequest request) {
        cardService.existsById(id);
        Card card = toMapper(request, Card.class);
        CardEntity entity = card.toEntity();
        cardService.save(entity);
        return ResponseEntity.ok(toMapper(entity, CardResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Card.DELETE_CARDS)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Card.SEARCH_CARDS_ICONS)
    public ResponseEntity<List<BankIconResponse>> findAllIcons() {
        List<BankIcon> icons = Stream.of(BankIcon.values())
                .sorted(Comparator.comparing(BankIcon::getBankName))
                .toList();
        return ResponseEntity.ok(toCollection(icons, BankIconResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Card.FILTER_CARDS)
    public ResponseEntity<List<CardResponse>> filterBy(String search) {
        List<CardEntity> entities = cardService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, CardResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Card.FILTER_CARDS)
    public ResponseEntity<List<CardResponse>> findAll() {
        return super.findAll();
    }

    @Override
    public Class<?> responseClass() {
        return CardResponse.class;
    }
}
