package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class GiftCertificateServiceImplTest {

    private GiftCertificateDAO giftCertificateDAO;
    private GiftCertificateService giftCertificateService;
    private int page;
    private int size;

    @BeforeEach
    public void init() {
        page = 1;
        size = 10;
        giftCertificateDAO = Mockito.mock(GiftCertificateDaoImpl.class);
        TagService tagService = Mockito.mock(TagServiceImpl.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDAO, tagService);
    }

    private List<GiftCertificate> initCertificates() {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("name" + i);

            certificates.add(certificate);
        }

        return certificates;
    }

    private GiftCertificate initCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificate.setName("Tourism");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(10);
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificate.setDuration(10);
        Tag tag = new Tag(1, "spa");
        giftCertificate.getTags().add(tag);

        return giftCertificate;
    }

    @Test
    void whenGetCertificate_thenCorrectlyReturnsItById() throws ServiceException {
        GiftCertificate given = initCertificate();

        Mockito.when(giftCertificateDAO.getById(given.getId())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.getById(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getById(given.getId());
    }

    @Test
    void whenGetCertificate_thenCorrectlyReturnsItByName() throws ServiceException {
        GiftCertificate given = initCertificate();

        Mockito.when(giftCertificateDAO.getByName(given.getName())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.getByName(given.getName());
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getByName(given.getName());
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThem() throws ServiceException {
        List<GiftCertificate> given = initCertificates();
        CertificateSearchCriteria givenSearchCriteria = CertificateSearchCriteria.getDefaultCertificateRequestBody();

        Mockito.when(giftCertificateDAO.getByRequestBody(givenSearchCriteria, size, page))
                .thenReturn(given);

        List<GiftCertificate> actual = giftCertificateService.getByPage(givenSearchCriteria, size, page,
                givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getByRequestBody(givenSearchCriteria, size, page);
    }

    @Test
    void whenTryAddEmptyCertificate_thenThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate();

        try {
            giftCertificateService.create(giftCertificate);
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to validate: certificate name is empty", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteCertificate_thenThrowException() {
        GiftCertificate givenCertificate = initCertificate();

        try {
            giftCertificateService.delete(givenCertificate.getId());
        } catch (ServiceException e) {
            Assertions.assertEquals(
                    "Failed to get certificate by id = " + givenCertificate.getId(), e.getMessage());
        }
    }

    @Test
    void whenAddCertificate_thenReturnThemSortedByDateAsc() throws ServiceException {
        CertificateSearchCriteria givenSearchCriteria = new CertificateSearchCriteria();
        givenSearchCriteria.setSortType(SortType.ASC);
        givenSearchCriteria.setSortBy(SortBy.CREATE_DATE);
        List<GiftCertificate> givenCertificates = initCertificates();

        Mockito.when(giftCertificateDAO.getByRequestBody(givenSearchCriteria, page, size))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.getByPage(
                givenSearchCriteria, page, size, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getByRequestBody(givenSearchCriteria, page, size);
    }

    @Test
    void whenAddCertificate_thenReturnThemSortedByDateDesc() throws ServiceException {
        CertificateSearchCriteria givenSearchCriteria = new CertificateSearchCriteria();
        givenSearchCriteria.setSortType(SortType.DESC);
        givenSearchCriteria.setSortBy(SortBy.CREATE_DATE);
        List<GiftCertificate> givenCertificates = initCertificates();

        Mockito.when(giftCertificateDAO.getByRequestBody(givenSearchCriteria, page, size))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.getByPage(
                givenSearchCriteria, page, size, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getByRequestBody(givenSearchCriteria, page, size);
    }
}
