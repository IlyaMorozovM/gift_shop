package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.service.util.UserValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final UserValidator userValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserValidator userValidator, PaginationValidator paginationValidator) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public User getUserByLogin(String login) throws ServiceException {
        userValidator.validateLogin(login);
        try {
            return userDao.getUserByLogin(login);
        } catch (NoResultException e) {
            LOGGER.error("Following exception was thrown in getUser(String login): " + e.getMessage());
            throw new ServiceException("Failed to get user by it login: " + login,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public User getUserById(int userId) throws ServiceException {
        userValidator.validateId(userId);
        try {
            User user = userDao.getUserById(userId);
            if (user == null) {
                LOGGER.error("Failed to get user by it id: " + userId);
                throw new ServiceException("Failed to get user by it id: " + userId, ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
            }

            return user;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getUser(int id): " + e.getMessage());
            throw new ServiceException("Failed to get user by it id: " + userId, ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public List<User> getAllUsersByPage(UserSearchCriteria searchCriteria, int page, int size,
                                        SortType sortType, SortBy sortBy) throws ServiceException {
        paginationValidator.validatePagination(page, size);

        if (searchCriteria == null) {
            searchCriteria = UserSearchCriteria.getDefaultUserRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);
        userValidator.validateUserSearchCriteria(searchCriteria);

        try {
            return userDao.getAllUsersByPage(searchCriteria, page, size);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllUsersByPage(): " + e.getMessage());
            throw new ServiceException("Failed to get users", ErrorCodeEnum.FAILED_TO_RETRIEVE_USER);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        paginationValidator.validateSize(size);
        try {
            return userDao.getLastPage(size);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }
}
