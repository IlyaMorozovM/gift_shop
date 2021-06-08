package com.epam.esm.dao;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;

import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code Order} in and out
 * of data source.
 *
 * @author Ilya Morozov
 */
public interface OrderDao {

    /**
     * Retrieves data of {@code Order} from
     * data source by user
     * which id equals to {@code int userId}.
     *
     * @param userId user id which orders to return.
     * @param searchCriteria object containing search criteria.
     * @param page page number of {@code Order} to return.
     * @param size page size of {@code Order} to return from data source.
     * @return {@code Order}s.
     */
    List<Order> getByUserId(int userId, OrderSearchCriteria searchCriteria, int page, int size);

    /**
     * Retrieves data of {@code Order} from
     * data source by it id
     * which equals to {@code int orderId}.
     *
     * @param orderId order id.
     * @return {@code Order}.
     */
    Order getById(int orderId);

    /**
     * Retrieves certain number of {@code Order} from data source.
     *
     * @param searchCriteria object containing search criteria.
     * @param page page number of {@code Order} to return.
     * @param size page size of {@code Order} to return from data source.
     * @return List<Order> - certain number of existing orders in data source.
     */
    List<Order> getAllByPage(OrderSearchCriteria searchCriteria, int page, int size);

    /**
     * Retrieves number of a last page from data source if every page
     * contains certain number of {@code Order}.
     *
     * @param size size of a page.
     * @return number of a last page.
     */
    int getLastPage(int size);

    /**
     * Adds new {@code Order} to data source.
     *
     * @param order {@code Order} which to be added to data source.
     * @return added {@code User} from data source.
     */
    Order add(Order order);

    /**
     * Deletes {@code Order} from data source by it id
     * which equals to {@code int orderId}.
     *
     * @param orderId id of a {@code Order} which to delete from data source.
     */
    void delete(int orderId);
}
