package com.epam.esm.dao.manager.impl;

import com.epam.esm.dao.manager.PersistenceManager;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.BaseModel;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
@EntityScan(basePackages = "com.epam.esm.model")
public class PersistenceManagerImpl<T extends BaseModel> implements PersistenceManager<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> type;

    @Override
    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getModelByName(String query, String name) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query, type);
        typedQuery.setParameter("name", name);
        return typedQuery.getResultList().get(0);
    }

    @Override
    public T getModelById(long modelId) {
        return entityManager.find(type, modelId);
    }

    @Override
    public List<T> getAllModels(String query) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query, type);
        return typedQuery.getResultList();
    }

    @Override
    public List<T> getAllModelsByPage(String query, int page, int size, SortType sortType, SortBy sortBy) {
        query += "order by " + sortBy + " " + sortType;
        TypedQuery<T> typedQuery = entityManager.createQuery(query, type);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public int getLastPage(String query, int size) {
        Query typedQuery = entityManager.createQuery(query);
        Long count = (Long) typedQuery.getSingleResult();

        int pages = count.intValue() / size;
        if (count % size > 0) {
            pages++;
        }

        return pages;
    }

    @Override
    public T add(T model) {
        entityManager.persist(model);
        entityManager.flush();
        return model;
    }

    @Override
    public void delete(long modelId) {
        T model = getModelById(modelId);
        if (model == null) {
            throw new NoResultException("Failed to find model to delete by id: " + modelId);
        }
        entityManager.remove(model);
    }

    @Override
    public T update(T model) {
        model = entityManager.merge(model);
        entityManager.flush();

        return model;
    }
}
