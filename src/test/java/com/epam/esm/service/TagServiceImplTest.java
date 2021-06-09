package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import com.epam.esm.service.util.impl.TagValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TagServiceImplTest {

    private TagDao tagDao;
    private TagService tagService;

    @BeforeEach
    public void init() {
        tagDao = Mockito.mock(TagDaoImpl.class);
        tagService = new TagServiceImpl(tagDao, new TagValidatorImpl(), new PaginationValidatorImpl());
    }

    @Test
    void whenGetTag_thenCorrectlyReturnItById() throws ServiceException {
        Tag given = new Tag(1, "spa");

        Mockito.when(tagDao.getById(given.getId())).thenReturn(given);

        Tag actual = tagService.getById(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(tagDao).getById(given.getId());
    }

    @Test
    void whenGetTag_thenCorrectlyReturnItByName() throws ServiceException {
        Tag given = new Tag(1, "spa");

        Mockito.when(tagDao.getByName(given.getName())).thenReturn(given);

        Tag actual = tagService.getByName(given.getName());
        Assertions.assertEquals(given, actual);
        Mockito.verify(tagDao).getByName(given.getName());
    }

    @Test
    void whenTryAddVoidTag_thenThrowException() {
        Tag tag = new Tag();

        try {
            tagService.create(tag);
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to validate: tag name is empty", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteNonExistingTag_thenThrowException() {
        Tag tag = new Tag(1, "spa");

        try {
            tagService.delete(tag.getId());
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to delete tag because it id ("
                    + tag.getId() +") is not found", e.getMessage());
        }
        Mockito.verify(tagDao).deleteById(tag.getId());
    }
}
