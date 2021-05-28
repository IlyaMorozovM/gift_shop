package com.epam.esm.web.hateoas;

import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftShopLinkBuilder {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final UserSearchCriteria userRequestBody = UserSearchCriteria.getDefaultUserRequestBody();
    private static final OrderSearchCriteria orderRequestBody = OrderSearchCriteria.getDefaultOrderRequestBody();
    private static final TagSearchCriteria tagRequestBody = TagSearchCriteria.getDefaultTagRequestBody();
    private static final CertificateSearchCriteria certificateRequestBody = CertificateSearchCriteria.getDefaultCertificateRequestBody();

    public List<Link> getRestApi() throws ServiceException {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(UserController.class).getUsers(userRequestBody, DEFAULT_PAGE, DEFAULT_SIZE,
                userRequestBody.getSortType(), userRequestBody.getSortBy())).withRel("users"));

        links.add(linkTo(methodOn(OrderController.class).getOrders(orderRequestBody, DEFAULT_PAGE, DEFAULT_SIZE,
                orderRequestBody.getSortType(), orderRequestBody.getSortBy())).withRel("orders"));

        links.add(linkTo(methodOn(TagController.class).getTags(tagRequestBody, DEFAULT_PAGE, DEFAULT_SIZE,
                tagRequestBody.getSortType(), tagRequestBody.getSortBy())).withRel("tags"));

        links.add(linkTo(methodOn(CertificateController.class).getGiftCertificates(
                certificateRequestBody, DEFAULT_PAGE, DEFAULT_SIZE,
                certificateRequestBody.getSortType(), certificateRequestBody.getSortBy())).withRel("certificates"));

        return links;
    }
}
