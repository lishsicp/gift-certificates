package com.epam.esm.assembler;

public interface ModelAssembler<T> {
    T toModel(T t);
}
