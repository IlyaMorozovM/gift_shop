package com.epam.esm.service;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

/**
 * This interface provides with ability to create
 * transactions with {@code Order} in and out
 * of persistence layer.
 *
 * @author Ilya Morozov
 */
public interface OrderService {

    /**
     * Retrieves {@code Order} from persistence layer.
     *
     * @param userId id of a user which orders to retrieve.
     * @param searchCriteria object containing search criteria.
     * @param page from which position in a data source.
     * @param size max amount of {@code Order} to return.
     * @param sortType type of a sort.
     * @param sortBy by witch field to sort.
     * @throws ServiceException when failed to get {@code Order}.
     * @return List<Order> - orders from persistence layer.
     */
    List<Order> getOrdersByUserId(int userId, OrderSearchCriteria searchCriteria, int page, int size,
                                  SortType sortType, SortBy sortBy) throws ServiceException;

    /**
     * Retrieves data of {@code Order} from
     * persistence layer by it id
     * which equals to {@code int orderId}.
     *
     * @param orderId order id.
     * @throws ServiceException when failed to get {@code Order}.
     * @return {@code Order}.
     */
    Order getOrderById(int orderId) throws ServiceException;

    /**
     * Retrieves {@code Order} from persistence layer.
     *
     * @param searchCriteria object containing search criteria.
     * @param page from which position to start.
     * @param size max amount of {@code GiftCertificate} to return.
     * @param sortType type of a sort.
     * @param sortBy by witch field to sort.
     * @throws ServiceException when failed to get {@code Order}.
     * @return List<Order> - orders from persistence layer.
     */
    List<Order> getAllOrdersByPage(OrderSearchCriteria searchCriteria, int page, int size,
                                   SortType sortType, SortBy sortBy) throws ServiceException;

    /**
     * Retrieves number of a last page from persistence layer if every page
     * contains certain number of {@code Order}.
     *
     * @param size size of a page.
     * @return number of a last page.
     */
    int getLastPage(int size) throws ServiceException;

    /**
     * Adds new {@code Order} to persistence layer.
     *
     * @param order {@code Order} which to add to persistence layer.
     * @throws ServiceException when failed to add {@code Order}.
     * @return added {@code Order} from persistence layer.
     */
    Order addOrder(Order order) throws ServiceException;

    /**
     * Deletes {@code Order} from persistence layer.
     *
     * @param orderId id of {@code Order} which to delete from persistence layer.
     * @throws ServiceException when failed to delete {@code Order}.
     */
    void deleteOrder(int orderId) throws ServiceException;
}
