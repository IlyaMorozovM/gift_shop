package com.epam.esm.web.controller;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.web.dto.GiftCertificateDto;
import com.epam.esm.web.hateoas.CertificateLinkBuilder;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.pagination.PaginationConfigurer;
import com.epam.esm.web.hateoas.pagination.impl.PaginationConfigurerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@RestController
public class CertificateController {

    private final GiftCertificateService giftCertificateService;
    private final ModelAssembler<GiftCertificateDto> modelAssembler;
    private final PaginationConfigurer<GiftCertificateDto> paginationConfigurer;

    @Autowired
    public CertificateController(
            GiftCertificateService giftCertificateService, ModelAssembler<GiftCertificateDto> modelAssembler,
            PaginationValidator paginationValidator) {
        this.giftCertificateService = giftCertificateService;
        this.modelAssembler = modelAssembler;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler, paginationValidator);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new CertificateLinkBuilder());
    }

    @GetMapping("/certificates")
    public CollectionModel<EntityModel<GiftCertificateDto>> getGiftCertificates(
            @RequestBody(required = false) CertificateSearchCriteria request,
            @RequestParam @Min(1) int page, @RequestParam @Min(1) int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, giftCertificateService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(GiftCertificateDto.of(
                giftCertificateService.getByPage(request, page, size, sortType, sortBy)));
    }

    @GetMapping("/certificates/{id}")
    public EntityModel<GiftCertificateDto> getGiftCertificate(@PathVariable @Min(1) int id) throws ServiceException {
        return modelAssembler.toModel(GiftCertificateDto.of(giftCertificateService.getById(id)));
    }

    @PostMapping("/certificates")
    public ResponseEntity<Object> addGiftCertificate(@Valid @RequestBody GiftCertificate giftCertificate)
            throws ServiceException {
        return new ResponseEntity<>(modelAssembler.toModel(
                GiftCertificateDto.of(giftCertificateService.create(giftCertificate))), HttpStatus.CREATED);
    }

    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable int id) throws ServiceException {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/certificates/{id}")
    public EntityModel<GiftCertificateDto> updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(
                GiftCertificateDto.of(giftCertificateService.update(giftCertificate, id)));
    }
}
