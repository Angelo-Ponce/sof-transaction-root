package com.sof.service.impl;

import com.sof.exception.ModelNotFoundException;
import com.sof.repository.IGenericRepository;
import com.sof.service.ICRUDService;

import java.util.List;

public abstract class CRUDServiceImpl <T, I> implements ICRUDService<T, I> {

    protected abstract IGenericRepository<T, I> getRepository();

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public T findById(I id) {
        return getRepository().findById(id).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
    }

    @Override
    public void deleteById(I id) {
        this.findById(id);
        getRepository().deleteById(id);
    }
}
