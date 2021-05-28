package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final PersistenceService<User> persistenceService;

    private static final String GET_USER_BY_NAME = "SELECT u FROM User u WHERE u.login=:login ";
    private static final String GET_ALL_USERS = "SELECT u FROM User u ";
    private static final String GET_USER_COUNT = "SELECT count(u.id) FROM User u ";

    @Autowired
    public UserDaoImpl(PersistenceService<User> persistenceService) {
       this.persistenceService = persistenceService;
    }

    @PostConstruct
    private void init() {
        persistenceService.setType(User.class);
    }

    @Override
    public User getUserByLogin(String login) {
        User user = persistenceService.getModelByName(GET_USER_BY_NAME, login);
        if (user == null) {
            throw new NoResultException();
        }
        removeDeletedOrdersFromUser(user);
        return user;
    }

    @Override
    public User getUserById(int userId) {
        User user = persistenceService.getModelById(userId);
        if (user == null) {
            throw new NoResultException();
        }
        removeDeletedOrdersFromUser(user);
        return user;
    }

    @Override
    public List<User> getAllUsersByPage(UserSearchCriteria searchCriteria, int page, int size) {
        List<User> users = persistenceService.getAllModelsByPage(
                GET_ALL_USERS, page, size, searchCriteria.getSortType(), searchCriteria.getSortBy());
        removeDeletedOrdersFromUsers(users);

        return users;
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_USER_COUNT, size);
    }

    private void removeDeletedOrdersFromUsers(List<User> users) {
        users.forEach(this::removeDeletedOrdersFromUser);
    }

    private void removeDeletedOrdersFromUser(User user) {
        user.getOrders().removeIf(o -> !o.isActive());
    }
}
