package com.epam.esm.web.controller;

import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.UserDto;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.OrderLinkBuilder;
import com.epam.esm.web.hateoas.UserLinkBuilder;
import com.epam.esm.web.hateoas.pagination.PaginationConfigurer;
import com.epam.esm.web.hateoas.pagination.impl.PaginationConfigurerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final ModelAssembler<UserDto> modelAssembler;
    private final ModelAssembler<OrderDto> orderModelAssembler;
    private final PaginationConfigurer<UserDto> paginationConfigurer;

    @Autowired
    public UserController(UserService userService, ModelAssembler<UserDto> modelAssembler,
                          ModelAssembler<OrderDto> orderModelAssembler, OrderService orderService) {
        this.userService = userService;
        this.modelAssembler = modelAssembler;
        this.orderModelAssembler = orderModelAssembler;
        this.orderService = orderService;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new UserLinkBuilder());
        orderModelAssembler.setModelLinkBuilder(new OrderLinkBuilder());
    }

    @GetMapping
    public CollectionModel<EntityModel<UserDto>> get(
            @RequestBody(required = false) UserSearchCriteria request,
            @RequestParam @Min(1) int page, @RequestParam @Min(1) @Max(100) int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, userService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(
                UserDto.of(userService.getAllByPage(request, page, size, sortType, sortBy)));
    }

    @GetMapping("/{id}")
    public EntityModel<UserDto> getById(@PathVariable long id) throws ServiceException {
        return modelAssembler.toModel(UserDto.of(userService.getById(id)));
    }

    @GetMapping("/{id}/orders")
    public CollectionModel<EntityModel<OrderDto>> getOrders(
            @RequestBody(required = false) OrderSearchCriteria requestBody,
            @RequestParam int page, @RequestParam int size, @PathVariable int id,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        return orderModelAssembler.toCollectionModel(
                OrderDto.of(orderService.getByUserId(id, requestBody, page, size, sortType, sortBy)));
    }
}
