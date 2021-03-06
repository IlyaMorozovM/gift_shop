package com.epam.esm.web.hateoas.pagination.impl;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.RepresentationModel;
import com.epam.esm.web.hateoas.pagination.PaginationConfigurer;
import org.springframework.hateoas.PagedModel;

public class PaginationConfigurerImpl<T> implements PaginationConfigurer<T> {

    private final ModelAssembler<T> modelAssembler;

    public PaginationConfigurerImpl(ModelAssembler<T> modelAssembler) {
        this.modelAssembler = modelAssembler;
    }

    @Override
    public void configure(int page, int size, int lastPage, SortType sortType, SortBy sortBy)
            throws ServiceException {
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                size, page, (long) lastPage * size, lastPage);
        RepresentationModel model = new RepresentationModel(pageMetadata, sortType, sortBy);
        modelAssembler.setRepresentationModel(model);
    }
}
