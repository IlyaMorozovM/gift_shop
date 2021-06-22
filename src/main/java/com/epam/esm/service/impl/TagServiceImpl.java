package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.dao.request.TagSearchCriteria;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private static final Logger LOGGER = LogManager.getLogger(TagServiceImpl.class);

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag getByName(String name) throws ServiceException {
        try {
            return tagDao.getByName(name);
        } catch (NoResultException ex) {
            throw new ServiceException(String.format("Failed to get tag with name = {%s}", name),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag getById(long tagId) throws ServiceException {
        try {
            Tag tag = tagDao.getById(tagId);
            if (tag == null) {
                LOGGER.error("Failed to get tag by id = " + tagId);
                throw new ServiceException("Failed to get tag by id = " + tagId,
                        ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
            }

            return tag;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(int id): " + e.getMessage());
            throw new ServiceException("Failed to get tag by id = " + tagId,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Tag> getAllByPage(TagSearchCriteria searchCriteria, int page, int size,
                                  SortType sortType, SortBy sortBy) throws ServiceException {
        if (searchCriteria == null) {
            searchCriteria = TagSearchCriteria.getDefaultTagRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);

        try {
            return tagDao.getAllByPage(searchCriteria, page, size);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllTagsByPage(): " + e.getMessage());
            throw new ServiceException("Failed to get tags", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        try {
            return tagDao.getLastPage(size);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }

    @Override
    public Tag getMostFrequentFromHighestCostUser() throws ServiceException {
        try {
            return tagDao.getMostFrequentFromHighestCostUser();
        } catch (DataAccessException e) {
            LOGGER.error("Failed to get most frequent tag");
            throw new ServiceException("Failed to get most frequent tag",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_ORDER);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Tag create(Tag tag) throws ServiceException {
        try {
            return tagDao.create(tag);
        } catch (PersistenceException | DataAccessException e) {
            LOGGER.error("Failed to add tag");
            throw new ServiceException("Failed to add tag", ErrorCodeEnum.FAILED_TO_ADD_TAG);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(long tagId) throws ServiceException {
        try {
            tagDao.deleteById(tagId);
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            LOGGER.error("Following exception was thrown in deleteTag(): " + e.getMessage());
            throw new ServiceException("Failed to delete tag by id = " + tagId,
                    ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }
}
