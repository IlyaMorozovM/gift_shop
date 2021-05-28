package com.epam.esm.web.hateoas;

import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.dto.GiftCertificateDto;
import com.epam.esm.web.dto.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateLinkBuilder implements ModelLinkBuilder<GiftCertificateDto> {

    private static ModelLinkBuilder<TagDto> tagLinkBuilder;

    private static final String ALL_CERTIFICATES = "certificates";
    private static final String CURRENT_CERTIFICATE = "self";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final CertificateSearchCriteria defaultRequestBody =
            CertificateSearchCriteria.getDefaultCertificateRequestBody();

    @PostConstruct
    public void init() {
        CertificateLinkBuilder.tagLinkBuilder = new TagLinkBuilder();
    }

    @Override
    public void linkToModel(EntityModel<GiftCertificateDto> modelDto) {
        Objects.requireNonNull(modelDto.getContent()).getTags().forEach(t -> tagLinkBuilder.linkToModel(t));
        try {
            modelDto.add(linkTo(methodOn(CertificateController.class).getGiftCertificate(
                    Objects.requireNonNull(modelDto.getContent()).getId())).withRel(CURRENT_CERTIFICATE));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToModelPage(CollectionModel<EntityModel<GiftCertificateDto>> collectionModel,
                                int page, int size, SortType sortType, SortBy sortBy) {
        try {
            collectionModel.add(getLinkToCertificatesPage(page, size, ALL_CERTIFICATES, sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToFirstModelPage(EntityModel<GiftCertificateDto> model, SortType sortType, SortBy sortBy) {
        try {
            model.add(getLinkToCertificatesPage(DEFAULT_PAGE, DEFAULT_SIZE, ALL_CERTIFICATES, sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToNextModelPage(CollectionModel<EntityModel<GiftCertificateDto>> collectionModel,
                                    int page, int size, SortType sortType, SortBy sortBy) {
        try {
            collectionModel.add(getLinkToCertificatesPage(page + 1, size, "next", sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToPrevModelPage(CollectionModel<EntityModel<GiftCertificateDto>> collectionModel,
                                    int page, int size, SortType sortType, SortBy sortBy) {
        try {
            collectionModel.add(getLinkToCertificatesPage(page - 1, size, "prev", sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToLastModelPage(CollectionModel<EntityModel<GiftCertificateDto>> collectionModel,
                                    int lastPage, int size, SortType sortType, SortBy sortBy) {
        try {
            collectionModel.add(getLinkToCertificatesPage(lastPage, size, "last", sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    private Link getLinkToCertificatesPage(int page, int size, String rel, SortType sortType, SortBy sortBy)
            throws ServiceException {
        return linkTo(methodOn(CertificateController.class)
                .getGiftCertificates(defaultRequestBody, page, size, sortType, sortBy)).withRel(rel);
    }
}
