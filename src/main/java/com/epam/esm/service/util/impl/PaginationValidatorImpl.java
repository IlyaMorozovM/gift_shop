package com.epam.esm.service.util.impl;

import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.PaginationValidator;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidatorImpl implements PaginationValidator {

    private static final int MAX_AMOUNT_OF_ITEMS = 100;

    @Override
    public void validatePagination(int page, int size) throws ServiceException {
        validateSize(size);

        if (page <= 0) {
            throw new ServiceException("Failed to validate: page must be positive",
                    ErrorCodeEnum.PAGINATION_VALIDATION_ERROR);
        }
    }

    @Override
    public void validateSize(int size) throws ServiceException {
        if (size <= 0) {
            throw new ServiceException("Failed to validate: size must be positive",
                    ErrorCodeEnum.PAGINATION_VALIDATION_ERROR);
        }
        if (size > MAX_AMOUNT_OF_ITEMS) {
            throw new ServiceException("Failed to validate: size is bigger then maximum allowed",
                    ErrorCodeEnum.PAGINATION_VALIDATION_ERROR);
        }
    }
}
