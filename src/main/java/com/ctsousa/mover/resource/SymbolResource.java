package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.mapper.Transform;
import com.ctsousa.mover.repository.SymbolRepository;
import com.ctsousa.mover.response.SymbolResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;

@RestController
@RequestMapping("/symbols")
public class SymbolResource {

    private final SymbolRepository symbolRepository;

    public SymbolResource(SymbolRepository symbolRepository) {
        this.symbolRepository = symbolRepository;
    }

    @GetMapping
    public ResponseEntity<List<SymbolResponse>> findAll() {
        List<SymbolEntity> entities = symbolRepository.findAll();
        return ResponseEntity.ok(toCollection(entities, SymbolResponse.class));
    }
}
