package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.manager.PersistenceManager;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final PersistenceManager<User> persistenceManager;

    private static final String GET_USER_BY_NAME = "SELECT u FROM User u WHERE u.login=:login ";
    private static final String GET_ALL_USERS = "SELECT u FROM User u ";
    private static final String GET_USER_COUNT = "SELECT count(u.id) FROM User u ";

    @Autowired
    public UserDaoImpl(PersistenceManager<User> persistenceManager) {
       this.persistenceManager = persistenceManager;
    }

    @PostConstruct
    private void init() {
        persistenceManager.setType(User.class);
    }

    @Override
    public User getByLogin(String login) {
        User user = persistenceManager.getModelByName(GET_USER_BY_NAME, login);
        if (user == null) {
            throw new NoResultException();
        }
        return user;
    }

    @Override
    public User getById(long userId) {
        User user = persistenceManager.getModelById(userId);
        if (user == null) {
            throw new NoResultException();
        }
        return user;
    }

    @Override
    public List<User> getAllByPage(UserSearchCriteria searchCriteria, int page, int size) {
        List<User> users = persistenceManager.getAllModelsByPage(
                GET_ALL_USERS, page, size, searchCriteria.getSortType(), searchCriteria.getSortBy());

        return users;
    }

    @Override
    public int getLastPage(int size) {
        return persistenceManager.getLastPage(GET_USER_COUNT, size);
    }

}
