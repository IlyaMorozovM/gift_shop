package com.epam.esm.service;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.impl.OrderDaoImpl;
import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.service.util.impl.OrderValidatorImpl;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class OrderServiceImplTest {

    private OrderDao orderDao;
    private OrderService orderService;
    private int page;
    private int size;

    private Order initOrder() {
        Order order = new Order();
        order.setId(1);
        order.setTotalCost(10.10);
        order.setActive(true);
        order.setUser(new User(1, "login", "password"));
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1);
        certificate.setName("certificate");
        certificate.setPrice(10.10);
        order.getGiftCertificateList().add(certificate);

        return order;
    }

    private Order initOrder(int id) {
        Order order = new Order();
        order.setId(id);
        order.setTotalCost(10.10);
        order.setActive(true);
        order.setUser(new User(1, "login", "password"));

        return order;
    }

    @BeforeEach
    public void init() {
        page = 1;
        size = 10;
        orderDao = Mockito.mock(OrderDaoImpl.class);
        UserService userService = Mockito.mock(UserServiceImpl.class);
        GiftCertificateService certificateService = Mockito.mock(GiftCertificateServiceImpl.class);
        TagService tagService = Mockito.mock(TagServiceImpl.class);
        orderService = new OrderServiceImpl(orderDao,
                new OrderValidatorImpl(), new PaginationValidatorImpl(), userService, certificateService,
                tagService);
    }

    @Test
    void whenGetOrder_thenCorrectlyReturnsItById() throws ServiceException {
        Order given = initOrder();

        Mockito.when(orderDao.getById(given.getId())).thenReturn(given);

        Order actual = orderService.getById(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(orderDao).getById(given.getId());
    }

    @Test
    void whenAddOrders_thenCorrectlyReturnThem() throws ServiceException {
        List<Order> given = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            given.add(initOrder(i));
        }
        OrderSearchCriteria givenSearchCriteria = OrderSearchCriteria.getDefaultOrderRequestBody();

        Mockito.when(orderDao.getAllByPage(givenSearchCriteria, size, page))
                .thenReturn(given);

        List<Order> actual = orderService.getAllByPage(givenSearchCriteria, size, page,
                givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(given, actual);
        Mockito.verify(orderDao).getAllByPage(givenSearchCriteria, size, page);
    }

    @Test
    void whenTryAddEmptyOrder_thenThrowException() {
        Order order = new Order();

        try {
            orderService.create(order);
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to validate: cost must be positive", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteOrder_thenThrowException() {
        Order given = initOrder();

        try {
            orderService.delete(given.getId());
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to get certificate by id = " + given.getId(), e.getMessage());
        }
    }

    @Test
    void whenAddOrder_thenReturnThemSortedByDateAsc() throws ServiceException {
        OrderSearchCriteria givenSearchCriteria = new OrderSearchCriteria();
        givenSearchCriteria.setSortType(SortType.ASC);
        givenSearchCriteria.setSortBy(SortBy.COST);
        List<Order> givenCertificates = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            givenCertificates.add(initOrder(i));
        }

        Mockito.when(orderDao.getAllByPage(givenSearchCriteria, page, size))
                .thenReturn(givenCertificates);

        List<Order> actual = orderService.getAllByPage(
                givenSearchCriteria, page, size, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(givenCertificates, actual);
        Mockito.verify(orderDao).getAllByPage(givenSearchCriteria, page, size);
    }

    @Test
    void whenAddOrder_thenReturnThemSortedByDateDesc() throws ServiceException {
        OrderSearchCriteria givenSearchCriteria = new OrderSearchCriteria();
        givenSearchCriteria.setSortType(SortType.DESC);
        givenSearchCriteria.setSortBy(SortBy.COST);
        List<Order> givenCertificates = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            givenCertificates.add(initOrder(i));
        }

        Mockito.when(orderDao.getAllByPage(givenSearchCriteria, page, size))
                .thenReturn(givenCertificates);

        List<Order> actual = orderService.getAllByPage(
                givenSearchCriteria, page, size, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(givenCertificates, actual);
        Mockito.verify(orderDao).getAllByPage(givenSearchCriteria, page, size);
    }
}
