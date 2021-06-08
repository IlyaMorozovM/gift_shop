package com.epam.esm.dao;

import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.model.User;

import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code User} in and out
 * of data source.
 *
 * @author Ilya Morozov
 */
public interface UserDao {

    /**
     * Retrieves data of {@code User} from
     * data source by it login
     * which equals to {@code String login}.
     *
     * @param login user login.
     * @return {@code User}.
     */
    User getByLogin(String login);

    /**
     * Retrieves data of {@code User} from
     * data source by it id
     * which equals to {@code int userId}.
     *
     * @param userId user userId.
     * @return {@code User}.
     */
    User getById(int userId);

    /**
     * Retrieves certain number of {@code User} from data source
     * filtered by {@code UserSearchCriteria}.
     *
     * @param searchCriteria object containing search criteria.
     * @param page from which position in a data source
     * to start returning.
     * @param size max amount of {@code Tag} to return.
     * @return List<User> - certain number of users from data source.
     */
    List<User> getAllByPage(UserSearchCriteria searchCriteria, int page, int size);

    /**
     * Retrieves number of a last page from data source if every page
     * contains certain number of {@code User}.
     *
     * @param size size of a page.
     * @return number of a last page.
     */
    int getLastPage(int size);
}
