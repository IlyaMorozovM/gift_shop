package com.epam.esm.dao;

import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.model.GiftCertificate;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code GiftCertificate} in and out
 * of data source.
 *
 * @author Ilya Morozov
 */
public interface GiftCertificateDAO {

    /**
     * Retrieves data of {@code GiftCertificate} from
     * data source by it name
     * which equals to {@code String name}.
     *
     * @param name certificate name.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getByName(String name);

    /**
     * Retrieves data of {@code GiftCertificate} from
     * data source by it id
     * which equals to {@code int certificateId}.
     *
     * @param certificateId certificate id.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getById(long certificateId);

    /**
     * Retrieves certain number of {@code GiftCertificate} from data source.
     *
     * @param searchCriteria object containing search criteria.
     * @param page page number of {@code GiftCertificate} to return.
     * @param size page size of {@code GiftCertificate} to return from data source.
     * @return List<GiftCertificate> - certain number of existing gift certificates in data source.
     */
    List<GiftCertificate> getByRequestBody(CertificateSearchCriteria searchCriteria, int page, int size);

    /**
     * Retrieves number of a last page from data source if every page
     * contains certain number of {@code GiftCertificate}.
     *
     * @param size size of a page.
     * @return number of a last page.
     */
    int getLastPage(int size);

    /**
     * Adds new {@code GiftCertificate} to data source.
     *
     * @param giftCertificate {@code GiftCertificate} which to be added to data source.
     * @return added {@code GiftCertificate} from data source.
     */
    GiftCertificate create(GiftCertificate giftCertificate) throws PersistenceException;

    /**
     * Deletes {@code GiftCertificate} from data source by it id
     * which equals to {@code int orderId}.
     *
     * @param certificateId  id of a {@code GiftCertificate} which to delete from data source.
     */
    void delete(long certificateId);

    /**
     * Updates {@code GiftCertificate} in data source.
     *
     * @param giftCertificate {@code ServiceException} which to update in data source.
     * @return updated {@code GiftCertificate} from data source.
     */
    GiftCertificate update(GiftCertificate giftCertificate);
}
