package com.epam.esm.dao;

import com.epam.esm.dao.impl.OrderDaoImpl;
import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.manager.PersistenceManager;
import com.epam.esm.dao.manager.impl.PersistenceManagerImpl;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

class OrderDaoImplTest {

    private OrderDao orderDao;
    private PersistenceManager<Order> service;

    private static final String GET_ALL_ORDERS = "SELECT o FROM Order o ";
    private static final String GET_ORDER_COUNT = "SELECT count(o.id) FROM Order o ";
    private int size;
    private int page;

    private Order initOrder() {
        Order order = new Order();
        order.setId(1);
        order.setTotalCost(10.10);
        order.setUser(new User(1, "login", "password", "Oleg", 20));

        return order;
    }

    private Order initOrder(int id) {
        Order order = new Order();
        order.setId(id);
        order.setTotalCost(10.10);
        order.setUser(new User(1, "login", "password", "Oleg", 20));

        return order;
    }

    @BeforeEach
    public void init() {
        size = 10;
        page = 1;
        service = Mockito.mock(PersistenceManagerImpl.class);
        orderDao = new OrderDaoImpl(service);
    }

    @Test
    void whenGivenOrder_thenCorrectlyReturnItById() {
        Order given = initOrder();

        Mockito.when(service.getModelById(given.getId())).thenReturn(given);

        Order actual = orderDao.getById(given.getId());
        Assertions.assertEquals(given, actual);
    }

    @Test
    void whenGivenPageSize_thenCorrectlyReturnLastPage() {
        int givenLastPage = 10;

        Mockito.when(service.getLastPage(GET_ORDER_COUNT, size)).thenReturn(givenLastPage);

        int actualLastPage = orderDao.getLastPage(size);
        Assertions.assertEquals(givenLastPage, actualLastPage);
    }

    @Test
    void whenGivenOrder_thenCorrectlyDeleteId() {
        Order given = initOrder();

        Mockito.doThrow(new IllegalArgumentException()).when(service).delete(given.getId());

        try {
            orderDao.delete(given.getId());
        } catch (NoResultException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void whenGivenOrder_thenCorrectlyAddId() {
        Order given = initOrder();

        Mockito.when(service.add(given)).thenReturn(given);

        Order actual = orderDao.create(given);
        Assertions.assertEquals(given, actual);
    }

    @Test
    void whenAddOrder_thenCorrectlyReturnsIt() {
        List<Order> given = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            given.add(initOrder(i));
        }
        OrderSearchCriteria givenSearchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();

        Mockito.when(service.getAllModelsByPage(GET_ALL_ORDERS, page, size,
                givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy())).thenReturn(given);
        List<Order> actual = orderDao.getAllByPage(givenSearchCriteria, page, size);

        Assertions.assertEquals(given, actual);
    }
}
