package com.epam.esm.service.util.impl;

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
    public void validate(Order order) throws ServiceException {
        validateCost(order.getGiftCertificateList(), order.getTotalCost());
    }

    private void validateCost(Set<GiftCertificate> certificates, double cost) throws ServiceException {
        double totalCost = certificates.stream().mapToDouble(GiftCertificate::getPrice).sum();
        if (totalCost != cost) {
            throw new ServiceException("Failed to validate: cost must be sum of prices of certificate",
                    ErrorCodeEnum.ORDER_VALIDATION_ERROR);
        }
    }
}
