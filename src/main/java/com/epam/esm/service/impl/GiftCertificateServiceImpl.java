package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.service.exception.ServiceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.time.*;
import java.util.List;
import java.util.Set;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateServiceImpl.class);

    private final GiftCertificateDao giftCertificateDao;
    private final TagService tagService;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagService tagService) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagService = tagService;
    }

    @Override
    public GiftCertificate getByName(String name) throws ServiceException {
        try {
            return giftCertificateDao.getByName(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(String name): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it name: " + name,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public GiftCertificate getById(long certificateId) throws ServiceException {
        try {
            GiftCertificate giftCertificate = giftCertificateDao.getById(certificateId);
            if (giftCertificate == null) {
                LOGGER.error("Certificate with id = " + certificateId + " not found");
                throw new ServiceException("Certificate with id = " + certificateId + " not found",
                        ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
            }

            return giftCertificate;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(int id): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by id = " + certificateId,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getByPage(CertificateSearchCriteria searchCriteria, int page, int size,
                                           SortType sortType, SortBy sortBy) throws ServiceException {
        if (searchCriteria == null) {
            searchCriteria = CertificateSearchCriteria.getDefaultCertificateRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);

        return giftCertificateDao.getByRequestBody(searchCriteria, page, size);
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        try {
            return giftCertificateDao.getLastPage(size);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public GiftCertificate create(GiftCertificate giftCertificate) throws ServiceException {
        try {

            LocalDateTime createdDate = LocalDateTime.now();
            giftCertificate.setCreateDate(createdDate);
            giftCertificate.setLastUpdateDate(createdDate);

            addCertificateTags(giftCertificate);

            return giftCertificateDao.create(giftCertificate);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Following exception was thrown in addGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to add certificate: certificate already exist",
                    ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
        }
    }

    @Override
    public void addCertificateTags(GiftCertificate giftCertificate) throws ServiceException {
        Set<Tag> tags = giftCertificate.getTags();
        for (Tag tag: tags){
            try {
                Tag savedTag = tagService.getByName(tag.getName());
                tag.setId(savedTag.getId());
            } catch (NoResultException | ServiceException e){
                tagService.create(tag);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(long certificateId) throws ServiceException {
        GiftCertificate giftCertificate = getById(certificateId);
        if (giftCertificate == null) {
            LOGGER.error("Failed to delete certificate: certificate not found");
            throw new ServiceException("Failed to delete certificate: certificate not found",
                    ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
        }
        try {
            giftCertificateDao.delete(giftCertificate.getId());
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            LOGGER.error("Following exception was thrown in deleteGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to delete certificate", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public GiftCertificate update(GiftCertificate giftCertificate, int id) throws ServiceException {
        giftCertificate.setId(id);
        try {
            if (giftCertificateDao.getById(id) != null) {
                GiftCertificate giftCertificateFromDB = giftCertificateDao.getById(id);
                giftCertificateFromDB.setLastUpdateDate(LocalDateTime.now());
                giftCertificateFromDB.setPrice(giftCertificate.getPrice());

                giftCertificate = giftCertificateDao.update(giftCertificateFromDB);


                if (giftCertificate == null) {
                    LOGGER.error("Failed to update certificate");
                    throw new ServiceException("Failed to update certificate",
                            ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
                }
                return giftCertificate;
                } else {
                    throw new ServiceException("Certificate with id = " + id + " not found",
                        ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
                }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in updateGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
        }
    }
}
