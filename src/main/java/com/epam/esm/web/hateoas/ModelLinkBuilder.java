package com.epam.esm.web.hateoas;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ModelLinkBuilder<T> {

    void linkToModel(EntityModel<T> modelDto);

    void linkToModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size,
                         SortType sortType, SortBy sortBy);

    void linkToFirstModelPage(EntityModel<T> tagDto, SortType sortType, SortBy sortBy);

    void linkToNextModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size,
                             SortType sortType, SortBy sortBy);

    void linkToPrevModelPage(CollectionModel<EntityModel<T>> collectionModel, int page, int size,
                             SortType sortType, SortBy sortBy);

    void linkToLastModelPage(CollectionModel<EntityModel<T>> collectionModel, int lastPage, int size,
                             SortType sortType, SortBy sortBy);
}
