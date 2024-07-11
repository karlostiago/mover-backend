package com.ctsousa.mover.core.mapper;

public interface MapperToDomainV2<D, E> {

    D toDomain(E request);
}
