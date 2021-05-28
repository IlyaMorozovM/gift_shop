package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final PersistenceService<Tag> persistenceService;

    private static final boolean ACTIVE_TAG = true;
    private static final boolean DELETED_TAG = false;

    private static final String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name=:name AND t.isActive=true ";
    private static final String GET_ALL_TAGS = "SELECT t FROM Tag t WHERE t.isActive=true ";
    private static final String GET_TAG_COUNT = "SELECT count(t.id) FROM Tag t WHERE t.isActive=true ";
    private static final String GET_MOST_FREQUENT_TAG =
            "SELECT tags.ID, tags.Active, tags.Name, count(tags.Name) AS count FROM Orders " +
                    "INNER JOIN OrderCertificate ON OrderCertificate.OrderId = Orders.id " +
                    "INNER JOIN GiftCertificates ON CertificateId = GiftCertificates.id " +
                    "INNER JOIN CertificateTag ON CertificateTag.CertificateId = GiftCertificates.id " +
                    "INNER JOIN tags ON CertificateTag.tagId = tags.id " +
                    "WHERE userId IN ( " +
                    "   SELECT userId FROM ( " +
                    "       SELECT Sum(Cost) sumCost, userId " +
                    "       FROM Orders " +
                    "       GROUP BY userId " +
                    "       ORDER BY sumCost DESC " +
                    "       LIMIT 1 " +
                    "   ) AS ids " +
                    ") " +
                    "GROUP BY tags.ID " +
                    "ORDER BY count DESC LIMIT 1";

    @Autowired
    public TagDaoImpl(PersistenceService<Tag> persistenceService) {
        this.persistenceService = persistenceService;
    }

    @PostConstruct
    private void init() {
        persistenceService.setType(Tag.class);
    }

    @Override
    public Tag getTagByName(String name) throws NoResultException {
        return persistenceService.getModelByName(GET_TAG_BY_NAME, name);
    }

    @Override
    public Tag getTagById(int tagId) {
        return persistenceService.getModelById(tagId);
    }

    @Override
    public Tag getMostFrequentTagFromHighestCostUser() {
        Query query = entityManager.createNativeQuery(
                GET_MOST_FREQUENT_TAG, Tag.class);
        return (Tag) query.getSingleResult();
    }

    @Override
    public List<Tag> getAllTagsByPage(TagSearchCriteria searchCriteria, int page, int size) {
        return persistenceService.getAllModelsByPage(
                GET_ALL_TAGS, page, size, searchCriteria.getSortType(), searchCriteria.getSortBy());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_TAG_COUNT, size);
    }

    @Override
    public Tag addTag(Tag tag) {
        tag.setActive(ACTIVE_TAG);
        return persistenceService.add(tag);
    }

    @Override
    public void deleteTagById(int tagId) {
        Tag tag = persistenceService.getModelById(tagId);
        if (tag == null) {
            throw new NoResultException("Failed to find tag to delete by id: " + tagId);
        }
        tag.setActive(DELETED_TAG);
        persistenceService.update(tag);
    }
}
