package com.ctsousa.mover.core.mapper;

public interface MapperToDomain<D, E> {

    D toDomain(E request);
}
