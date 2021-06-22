package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.OrderValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);

    private final OrderDao orderDao;
    private final OrderValidator orderValidator;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, OrderValidator orderValidator, UserService userService,
                            GiftCertificateService giftCertificateService) {
        this.orderDao = orderDao;
        this.orderValidator = orderValidator;
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Order> getByUserId(int userId, OrderSearchCriteria searchCriteria, int page, int size,
                                   SortType sortType, SortBy sortBy) throws ServiceException {
        if (searchCriteria == null) {
            searchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);
        orderValidator.validateOrderSearchCriteria(searchCriteria);

        try {
            return orderDao.getByUserId(userId, searchCriteria, page, size);
        } catch (DataAccessException e) {
            LOGGER.error(String.format("Failed to get order by user id = {%s}", userId));
            throw new ServiceException(String.format("Failed to get order by user id = {%s}", userId),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    public Order getById(long orderId) throws ServiceException {
        orderValidator.validateId(orderId);

        try {
            Order order = orderDao.getById(orderId);
            if (order == null) {
                LOGGER.error(String.format("Failed to get order by user id = {%s}", orderId));
                throw new ServiceException(String.format("Failed to get order by user id = {%s}", orderId),
                        ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
            }

            return order;
        } catch (DataAccessException e) {
            LOGGER.error(String.format("Failed to get order by user id = {%s}", orderId));
            throw new ServiceException(String.format("Failed to get order by user id = {%s}", orderId),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    public List<Order> getAllByPage(OrderSearchCriteria searchCriteria, int page, int size,
                                    SortType sortType, SortBy sortBy) throws ServiceException {
        if (searchCriteria == null) {
            searchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);
        orderValidator.validateOrderSearchCriteria(searchCriteria);

        try {
            return orderDao.getAllByPage(searchCriteria, page, size);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to get orders by page");
            throw new ServiceException("Failed to get orders by page", ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        try {
            return orderDao.getLastPage(size);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Order create(Order order) throws ServiceException {
        orderValidator.validate(order);

        order.setCreateDate(LocalDateTime.now());

        try {
            User userOfOrder = userService.getById(order.getUser().getId());
            order.setUser(userOfOrder);

            for (GiftCertificate giftCertificate: order.getGiftCertificateList()) {
                GiftCertificate giftCertificateOfOrder = giftCertificateService.getByName(giftCertificate.getName());
                giftCertificate.setId(giftCertificateOfOrder.getId());
                giftCertificateService.addCertificateTags(giftCertificate);
            }
        } catch (ServiceException | NoResultException e){
            LOGGER.error("Failed to add order");
            throw new ServiceException("Failed to add order", ErrorCodeEnum.FAILED_TO_ADD_ORDER);
        }

        try {
            return orderDao.create(order);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Failed to add order");
            throw new ServiceException("Failed to add order", ErrorCodeEnum.FAILED_TO_ADD_ORDER);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(long orderId) throws ServiceException {
        orderValidator.validateId(orderId);
        try {
            orderDao.delete(orderId);
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            LOGGER.error("Failed to delete order");
            throw new ServiceException("Failed to delete order", ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }
}
