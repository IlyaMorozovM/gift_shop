package com.epam.esm.service.util;

import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.model.User;
import com.epam.esm.service.exception.ServiceException;

/**
 * This interface provides with ability to validate
 * given {@code User} or it specific field.
 *
 * @author Ilya Morozov
 */
public interface UserValidator {

    /**
     * Validates {@code User}.
     *
     * @param user {@code User} to be validated.
     * @throws ServiceException if {@code User} is incorrect.
     */
    void validate(User user) throws ServiceException;

    /**
     * Validates id of {@code User}.
     *
     * @param id id of {@code User} to be validated.
     * @throws ServiceException if id of {@code User} is incorrect.
     */
    void validateId(int id) throws ServiceException;

    /**
     * Validates login of {@code User}.
     *
     * @param login login of {@code User} to be validated.
     * @throws ServiceException if login of {@code User} is incorrect.
     */
    void validateLogin(String login) throws ServiceException;

    /**
     * Validates password of {@code User}.
     *
     * @param password password of {@code User} to be validated.
     * @throws ServiceException if password of {@code User} is incorrect.
     */
    void validatePassword(String password) throws ServiceException;

    /**
     * Validates searchCriteria of {@code User}.
     *
     * @param searchCriteria searchCriteria of {@code User} to be validated.
     * @throws ServiceException if searchCriteria of {@code User} is incorrect.
     */
    void validateUserSearchCriteria(UserSearchCriteria searchCriteria) throws ServiceException;
}
