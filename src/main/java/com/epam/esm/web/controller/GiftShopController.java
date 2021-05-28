package com.epam.esm.web.controller;

import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.hateoas.GiftShopLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GiftShopController {

    private final GiftShopLinkBuilder giftShopLinkBuilder;

    @Autowired
    public GiftShopController(GiftShopLinkBuilder giftShopLinkBuilder) {
        this.giftShopLinkBuilder = giftShopLinkBuilder;
    }

    @GetMapping()
    public List<Link> home() throws ServiceException {
        return giftShopLinkBuilder.getRestApi();
    }
}
