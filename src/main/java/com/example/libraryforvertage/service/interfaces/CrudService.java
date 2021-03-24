package com.example.libraryforvertage.service.interfaces;

public interface CrudService<T> {
    T create(T t);
    T getById(Long id);
    void update(T t, Long id);
    void remove(Long id);

}
