package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.mapper.SymbolMapper;
import com.ctsousa.mover.repository.SymbolRepository;
import com.ctsousa.mover.response.SymbolResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/symbols")
public class SymbolResource {

    private final SymbolMapper mapper;

    private final SymbolRepository symbolRepository;

    public SymbolResource(SymbolMapper mapper, SymbolRepository symbolRepository) {
        this.mapper = mapper;
        this.symbolRepository = symbolRepository;
    }

    @GetMapping
    public ResponseEntity<List<SymbolResponse>> findAll() {
        List<SymbolEntity> entities = symbolRepository.findAll();
        return ResponseEntity.ok(mapper.toCollections(entities));
    }
}
