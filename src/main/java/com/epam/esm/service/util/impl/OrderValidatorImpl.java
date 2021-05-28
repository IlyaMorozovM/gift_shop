package com.epam.esm.service.util.impl;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.OrderValidator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderValidatorImpl implements OrderValidator {

    @Override
    public void validateOrder(Order order) throws ServiceException {
        if (order == null) {
            throw new ServiceException("Failed to validate: order is empty",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }

        validateId(order.getId());
        validateCost(order.getGiftCertificateList(), order.getTotalCost());
        validateUserId(order.getUser().getId());
    }

    @Override
    public void validateId(int id) throws ServiceException {
        if (id < 0) {
            throw new ServiceException("Failed to validate: order id is negative",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
    }

    @Override
    public void validateOrderSearchCriteria(OrderSearchCriteria searchCriteria) throws ServiceException {
        if (!searchCriteria.getSortBy().equals(SortBy.COST)) {
            throw new ServiceException("Cant sort orders by " + searchCriteria.getSortBy(),
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
    }

    private void validateUserId(int userId) throws ServiceException {
        if (userId <= 0) {
            throw new ServiceException("Failed to validate: user id is negative",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
    }

    private void validateCost(Set<GiftCertificate> certificates, double cost) throws ServiceException {
        double totalCost = certificates.stream().mapToDouble(GiftCertificate::getPrice).sum();
        if (cost <= 0) {
            throw new ServiceException("Failed to validate: cost must be positive",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
        if (totalCost != cost) {
            throw new ServiceException("Failed to validate: cost must be sum of prices of certificate",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
    }
}
