package com.epam.esm.web.controller;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.web.dto.TagDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.TagLinkBuilder;
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
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final ModelAssembler<TagDto> modelAssembler;
    private final PaginationConfigurer<TagDto> paginationConfigurer;

    @Autowired
    public TagController(TagService tagService, ModelAssembler<TagDto> modelAssembler,
                         PaginationValidator paginationValidator) {
        this.tagService = tagService;
        this.modelAssembler = modelAssembler;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler, paginationValidator);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new TagLinkBuilder());
    }

    @GetMapping
    public CollectionModel<EntityModel<TagDto>> getTags(
            @RequestBody(required = false) TagSearchCriteria requestBody,
            @RequestParam @Min(1) int page, @RequestParam @Min(1) int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, tagService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(
                TagDto.of(tagService.getAllByPage(requestBody, page, size, sortType, sortBy)));
    }

    @GetMapping("/{id}")
    public EntityModel<TagDto> getTag(@PathVariable @Min(1) int id) throws ServiceException {
        return modelAssembler.toModel(TagDto.of(tagService.getById(id)));
    }

    @GetMapping("/tag")
    public EntityModel<TagDto> getMostFrequentTag() throws ServiceException {
        return modelAssembler.toModel(TagDto.of(tagService.getMostFrequentFromHighestCostUser()));
    }

    @PostMapping
    public ResponseEntity<Object> addTag(@Valid @RequestBody Tag tag) throws ServiceException {
        return new ResponseEntity<>(modelAssembler.toModel(TagDto.of(tagService.create(tag))), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable int id) throws ServiceException {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
