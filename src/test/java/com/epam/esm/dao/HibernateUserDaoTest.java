package com.epam.esm.dao;

import com.epam.esm.dao.impl.UserDaoImpl;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.dao.service.impl.PersistenceServiceImpl;
import com.epam.esm.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.NoResultException;

class HibernateUserDaoTest {

    private UserDao userDao;
    private PersistenceService<User> service;

    private static final String GET_USER_BY_NAME = "SELECT u FROM User u WHERE u.login=:login ";
    private static final String GET_USER_COUNT = "SELECT count(u.id) FROM User u ";
    private int size;

    private User initUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("login");
        user.setPassword("password");

        return user;
    }

    @BeforeEach
    public void init() {
        size = 10;
        service = Mockito.mock(PersistenceServiceImpl.class);
        userDao = new UserDaoImpl(service);
    }

    @Test
    void whenGivenUser_thenCorrectlyReturnItById() {
        User given = initUser();

        Mockito.when(service.getModelById(given.getId())).thenReturn(given);

        User actual = userDao.getUserById(given.getId());
        Assertions.assertEquals(given, actual);
    }

    @Test
    void whenGivenUser_thenCorrectlyReturnItByLogin() {
        User given = initUser();

        Mockito.when(service.getModelByName(GET_USER_BY_NAME, given.getLogin())).thenReturn(given);

        User actual = userDao.getUserByLogin(given.getLogin());
        Assertions.assertEquals(given, actual);
    }

    @Test
    void whenGivenPageSize_thenCorrectlyReturnLastPage() {
        int givenLastPage = 10;

        Mockito.when(service.getLastPage(GET_USER_COUNT, size)).thenReturn(givenLastPage);

        int actualLastPage = userDao.getLastPage(size);
        Assertions.assertEquals(givenLastPage, actualLastPage);
    }

    @Test
    void whenGivenUser_thenCorrectlyDeleteId() {
        User given = initUser();

        Mockito.doThrow(new IllegalArgumentException()).when(service).delete(given.getId());

        try {
            userDao.getUserById(given.getId());
        } catch (NoResultException e) {
            Assertions.assertTrue(true);
        }
    }
}
