package com.epam.esm.web.controller;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.OrderLinkBuilder;
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
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelAssembler<OrderDto> modelAssembler;
    private final PaginationConfigurer<OrderDto> paginationConfigurer;

    @Autowired
    public OrderController(OrderService orderService, ModelAssembler<OrderDto> modelAssembler,
                           PaginationValidator paginationValidator) {
        this.orderService = orderService;
        this.modelAssembler = modelAssembler;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler, paginationValidator);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new OrderLinkBuilder());
    }

    @GetMapping
    public CollectionModel<EntityModel<OrderDto>> get(
            @RequestBody(required = false) OrderSearchCriteria requestBody,
            @RequestParam @Min(1) int page, @RequestParam @Min(1) int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, orderService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(
                OrderDto.of(orderService.getAllByPage(requestBody, page, size, sortType, sortBy)));
    }

    @GetMapping("/{id}")
    public EntityModel<OrderDto> getById(@PathVariable long id) throws ServiceException {
        return modelAssembler.toModel(OrderDto.of(orderService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody Order order) throws ServiceException {
        return new ResponseEntity<>(modelAssembler.toModel(OrderDto.of(orderService.create(order))), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) throws ServiceException {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
