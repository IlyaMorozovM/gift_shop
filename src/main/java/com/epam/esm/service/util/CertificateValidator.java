package com.epam.esm.service.util;

import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;

/**
 * This interface provides with ability to validate
 * given {@code GiftCertificate} or it specific field.
 *
 * @author Ilya Morozov
 */
public interface CertificateValidator {

    /**
     * Validates {@code GiftCertificate}.
     *
     * @param giftCertificate {@code GiftCertificate} to be validated.
     * @throws ServiceException if {@code GiftCertificate} is incorrect.
     */
    void validate(GiftCertificate giftCertificate) throws ServiceException;

    /**
     * Validates {@code GiftCertificate} id.
     *
     * @param id id of {@code GiftCertificate} to be validated.
     * @throws ServiceException if id of {@code GiftCertificate} is incorrect.
     */
    void validateId(long id) throws ServiceException;

    /**
     * Validates {@code GiftCertificate} id.
     *
     * @param name name of {@code GiftCertificate} to be validated.
     * @throws ServiceException if name of {@code GiftCertificate} is incorrect.
     */
    void validateName(String name) throws ServiceException;

    /**
     * Validates {@code GiftCertificate} description.
     *
     * @param description description of {@code GiftCertificate} to be validated.
     * @throws ServiceException if description of {@code GiftCertificate} is incorrect.
     */
    void validateDescription(String description) throws ServiceException;

    /**
     * Validates {@code GiftCertificate} searchCriteria.
     *
     * @param searchCriteria searchCriteria of {@code GiftCertificate} to be validated.
     * @throws ServiceException if searchCriteria of {@code GiftCertificate} is incorrect.
     */
    void validateCertificateSearchCriteria(CertificateSearchCriteria searchCriteria) throws ServiceException;
}
