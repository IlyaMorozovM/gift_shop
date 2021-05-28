package com.epam.esm.service.util;

import com.epam.esm.service.exception.ServiceException;

/**
 * This interface provides with ability to validate
 * entities related to pagination.
 *
 * @author Ilya Morozov
 */
public interface PaginationValidator {

    /**
     * Validates page and size.
     *
     * @param page given page number.
     * @param size given page size.
     * @throws ServiceException if validation is incorrect.
     */
    void validatePagination(int page, int size) throws ServiceException;

    /**
     * Validates page size.
     *
     * @param size given page size.
     * @throws ServiceException if validation is incorrect.
     */
    void validateSize(int size) throws ServiceException;
}
