package com.sof.service;

import java.util.List;

public interface ICRUDService <T, I> {
    List<T> findAll();
    T findById(I id);
    void deleteById(I id);
}
