package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.dao.manager.PersistenceManager;
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

    private final PersistenceManager<Tag> persistenceManager;

    private static final String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name=:name ";
    private static final String GET_ALL_TAGS = "SELECT t FROM Tag t ";
    private static final String GET_TAG_COUNT = "SELECT count(t.id) FROM Tag t ";
    private static final String GET_MOST_FREQUENT_TAG =
            "SELECT tag.id, tag.name, count(tag.name) AS count FROM Orders " +
                    "INNER JOIN OrderCertificate ON OrderCertificate.OrderId = Orders.id " +
                    "INNER JOIN gift_certificate ON certificateId = gift_certificate.id " +
                    "INNER JOIN certificate_tag_m2m ON certificate_tag_m2m.certificate_id = gift_certificate.id " +
                    "INNER JOIN tag ON certificate_tag_m2m.tag_id = tag.id " +
                    "WHERE userId IN ( " +
                    "   SELECT userId FROM ( " +
                    "       SELECT Sum(Cost) sumCost, userId " +
                    "       FROM Orders " +
                    "       GROUP BY userId " +
                    "       ORDER BY sumCost DESC " +
                    "       LIMIT 1 " +
                    "   ) AS ids " +
                    ") " +
                    "GROUP BY tag.id " +
                    "ORDER BY count DESC LIMIT 1";

    @Autowired
    public TagDaoImpl(PersistenceManager<Tag> persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    @PostConstruct
    private void init() {
        persistenceManager.setType(Tag.class);
    }

    @Override
    public Tag getByName(String name) throws NoResultException {
        return persistenceManager.getModelByName(GET_TAG_BY_NAME, name);
    }

    @Override
    public Tag getById(int tagId) {
        return persistenceManager.getModelById(tagId);
    }

    @Override
    public Tag getMostFrequentFromHighestCostUser() {
        Query query = entityManager.createNativeQuery(
                GET_MOST_FREQUENT_TAG, Tag.class);
        return (Tag) query.getSingleResult();
    }

    @Override
    public List<Tag> getAllByPage(TagSearchCriteria searchCriteria, int page, int size) {
        return persistenceManager.getAllModelsByPage(
                GET_ALL_TAGS, page, size, searchCriteria.getSortType(), searchCriteria.getSortBy());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceManager.getLastPage(GET_TAG_COUNT, size);
    }

    @Override
    public Tag create(Tag tag) {
        return persistenceManager.add(tag);
    }

    @Override
    public void deleteById(int tagId) {
        Tag tag = persistenceManager.getModelById(tagId);
        if (tag == null) {
            throw new NoResultException("Failed to find tag to delete by id: " + tagId);
        }
        persistenceManager.delete(tagId);
    }
}
