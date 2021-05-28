package com.epam.esm.service.util.impl;

import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.model.User;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.UserValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidatorImpl implements UserValidator {

    @Override
    public void validateUser(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("Failed to validate: user is empty",
                    ErrorCodeEnum.USER_VALIDATION_ERROR);
        }

        validateId(user.getId());
        validateLogin(user.getLogin());
        validatePassword(user.getPassword());
    }

    @Override
    public void validateId(int id) throws ServiceException {
        if (id < 0) {
            throw new ServiceException("Failed to validate: id is negative",
                    ErrorCodeEnum.USER_VALIDATION_ERROR);
        }
    }

    @Override
    public void validateLogin(String login) throws ServiceException {
        if (login == null || login.isEmpty()) {
            throw new ServiceException("Failed to validate: login is empty",
                    ErrorCodeEnum.USER_VALIDATION_ERROR);
        }
    }

    @Override
    public void validatePassword(String password) throws ServiceException {
        if (password == null || password.isEmpty()) {
            throw new ServiceException("Failed to validate: login is empty",
                    ErrorCodeEnum.USER_VALIDATION_ERROR);
        }
    }

    @Override
    public void validateUserSearchCriteria(UserSearchCriteria searchCriteria) throws ServiceException {
        if (!searchCriteria.getSortBy().equals(SortBy.LOGIN)) {
            throw new ServiceException("Cant sort users by " + searchCriteria.getSortBy(),
                    ErrorCodeEnum.USER_VALIDATION_ERROR);
        }
    }
}
