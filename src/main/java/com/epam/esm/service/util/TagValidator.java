package com.epam.esm.service.util;

import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

/**
 * This interface provides with ability to validate
 * given {@code Tag} or it specific field.
 *
 * @author Ilya Morozov
 */
public interface TagValidator {

    /**
     * Validates {@code Tag}.
     *
     * @param tag {@code Tag} to be validated.
     * @throws ServiceException if {@code Tag} is incorrect.
     */
    void validateTag(Tag tag) throws ServiceException;

    /**
     * Validates name of {@code Tag}.
     *
     * @param name name of {@code Tag} to be validated.
     * @throws ServiceException if name of {@code Tag} is incorrect.
     */
    void validateName(String name) throws ServiceException;

    /**
     * Validates searchCriteria of {@code Tag}.
     *
     * @param searchCriteria searchCriteria of {@code Tag} to be validated.
     * @throws ServiceException if searchCriteria of {@code Tag} is incorrect.
     */
    void validateTagSearchCriteria(TagSearchCriteria searchCriteria) throws ServiceException;
}
