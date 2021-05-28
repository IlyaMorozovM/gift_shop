package com.epam.esm.dao.service;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.BaseModel;

import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code BaseModel} in and out
 * of data source.
 *
 * @author Ilya Morozov
 */
public interface PersistenceService<T extends BaseModel> {

    /**
     * Sets type of {@code BaseModel}.
     *
     * @param type type of {@code BaseModel}.
     */
    void setType(Class<T> type);

    /**
     * Retrieves data of {@code BaseModel} from
     * data source by name
     * which equals to {@code String name} using HQL query.
     *
     * @param name BaseModel name.
     * @param query HQL query.
     * @return {@code BaseModel}.
     */
    T getModelByName(String query, String name);

    /**
     * Retrieves data of {@code BaseModel} from
     * data source by id
     * which equals to {@code int modelId}.
     *
     * @param modelId BaseModel id.
     * @return {@code BaseModel}.
     */
    T getModelById(int modelId);

    /**
     * Retrieves all {@code BaseModel} from data source
     * using HQL query.
     *
     * @param query HQL query.
     * @return List<BaseModel> - all existing BaseModel in data source.
     */
    List<T> getAllModels(String query);

    /**
     * Retrieves all {@code GiftCertificate} from data source.
     *
     * @param query HQL query.
     * @param page from which position in a data source.
     * @param size max amount of {@code GiftCertificate} to return.
     * @param sortType sort type.
     * @param sortBy by which param to sort.
     * @return List<GiftCertificate> - all existing certificates in data source.
     */
    List<T> getAllModelsByPage(String query, int page, int size, SortType sortType, SortBy sortBy);

    /**
     * Retrieves last page of {@code BaseModel} from data source
     * using HQL query.
     *
     * @param query HQL query.
     * @param size size of a page.
     * @return last page of {@code BaseModel}.
     */
    int getLastPage(String query, int size);

    /**
     * Adds new {@code BaseModel} to data source.
     *
     * @param model {@code BaseModel} which to be added to data source.
     * @return added {@code BaseModel} from data source.
     */
    T add(T model);

    /**
     * Deletes {@code BaseModel} from data source.
     *
     * @param modelId id of {@code BaseModel} which to deleted from data source.
     */
    void delete(int modelId);

    /**
     * Updates {@code BaseModel} in data source.
     *
     * @param model {@code BaseModel} which to update in data source.
     * @return updated {@code BaseModel} from data source.
     */
    T update(T model);
}
