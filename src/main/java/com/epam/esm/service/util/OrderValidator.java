package com.epam.esm.service.util;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.model.Order;
import com.epam.esm.service.exception.ServiceException;

/**
 * This interface provides with ability to validate
 * given {@code Order} or it specific field.
 *
 * @author Ilya Morozov
 */
public interface OrderValidator {

    /**
     * Validates {@code Order}.
     *
     * @param order {@code Order} to be validated.
     * @throws ServiceException if {@code Order} is incorrect.
     */
    void validate(Order order) throws ServiceException;
}
