package com.epam.esm.web.hateoas.pagination;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ServiceException;

public interface PaginationConfigurer<T> {

    void configure(int page, int size, int lastPage, SortType sortType, SortBy sortBy) throws ServiceException;
}
