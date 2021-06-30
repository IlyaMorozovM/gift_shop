package com.epam.esm.web.controller;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.service.exception.ServiceException;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final GiftCertificateService giftCertificateService;
    private final ModelAssembler<GiftCertificateDto> modelAssembler;
    private final PaginationConfigurer<GiftCertificateDto> paginationConfigurer;

    @Autowired
    public CertificateController(
            GiftCertificateService giftCertificateService, ModelAssembler<GiftCertificateDto> modelAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.modelAssembler = modelAssembler;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new CertificateLinkBuilder());
    }

    @GetMapping
    public CollectionModel<EntityModel<GiftCertificateDto>> get(
            @RequestBody(required = false) CertificateSearchCriteria request,
            @RequestParam @Min(1) int page, @RequestParam @Min(1) @Max(100) int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, giftCertificateService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(GiftCertificateDto.of(
                giftCertificateService.getByPage(request, page, size, sortType, sortBy)));
    }

    @GetMapping("/{id}")
    public EntityModel<GiftCertificateDto> getById(@PathVariable long id) throws ServiceException {
        return modelAssembler.toModel(GiftCertificateDto.of(giftCertificateService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody GiftCertificate giftCertificate)
            throws ServiceException {
        return new ResponseEntity<>(modelAssembler.toModel(
                GiftCertificateDto.of(giftCertificateService.create(giftCertificate))), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) throws ServiceException {
        giftCertificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public EntityModel<GiftCertificateDto> update(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(
                GiftCertificateDto.of(giftCertificateService.update(giftCertificate, id)));
    }
}
