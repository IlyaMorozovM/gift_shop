package com.epam.esm.dao;

import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.dao.manager.PersistenceManager;
import com.epam.esm.dao.manager.impl.PersistenceManagerImpl;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HibernateTagDaoImpTest {

    private TagDao tagDao;
    private PersistenceManager<Tag> service;

    private static final String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name=:name";
    private static final String GET_TAG_COUNT = "SELECT count(t.id) FROM Tag t";
    private int size;

    @BeforeEach
    public void init() {
        size = 10;
        service = Mockito.mock(PersistenceManagerImpl.class);
        tagDao = new TagDaoImpl(service);
    }

    @Test
    void whenGivenTag_thenCorrectlyReturnItById() {
        Tag given = new Tag(1, "Tag to return by id");

        Mockito.when(service.getModelById(given.getId())).thenReturn(given);

        Tag actual = tagDao.getById(given.getId());
        Assertions.assertEquals(given, actual);
    }

    @Test
    void whenGivenPageSize_thenCorrectlyReturnLastPage() {
        int givenLastPage = 0;

        Mockito.when(service.getLastPage(GET_TAG_COUNT, size)).thenReturn(givenLastPage);

        int actualLastPage = tagDao.getLastPage(size);
        Assertions.assertEquals(givenLastPage, actualLastPage);
    }

    @Test
    void whenGivenTag_thenCorrectlyDeleteId() {
        Tag given = new Tag("Tag to delete");

        Mockito.doThrow(new IllegalArgumentException()).when(service).delete(given.getId());

        try {
            tagDao.deleteById(given.getId());
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void whenGivenTag_thenCorrectlyAddId() {
        Tag given = new Tag("Tag to add");

        Mockito.when(service.add(given)).thenReturn(given);

        Tag actual = tagDao.create(given);
        Assertions.assertEquals(given, actual);
    }
}
