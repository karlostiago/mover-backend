package com.ctsousa.mover.core.api.resource;

import com.ctsousa.mover.core.mapper.Transform;
import com.ctsousa.mover.core.service.BaseService;
import java.util.List;

public abstract class SimpleBaseResource<RESPONSE, REQUEST, T> {

    private final BaseService<T, Long> service;

    public SimpleBaseResource(BaseService<T, Long> service) {
        this.service = service;
    }

    /**
     * Transforma uma entidade em um objeto de resposta.
     */
    protected RESPONSE toResponse(T entity) {
        return Transform.toMapper(entity, responseClass());
    }

    /**
     * Transforma uma lista de entidades em uma lista de objetos de resposta.
     */
    protected List<RESPONSE> toResponseList(List<T> entities) {
        return Transform.toCollection(entities, responseClass());
    }

    /**
     * Transforma um objeto de solicitação (REQUEST) em uma entidade.
     */
    protected T toEntity(REQUEST request) {
        return Transform.toMapper(request, entityClass());
    }

    /**
     * Método abstrato para fornecer a classe de resposta para o mapeamento.
     */
    protected abstract Class<RESPONSE> responseClass();

    /**
     * Método abstrato para fornecer a classe da entidade para o mapeamento.
     */
    protected abstract Class<T> entityClass();
}
