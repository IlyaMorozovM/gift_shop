package com.epam.esm.web.hateoas;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.dto.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagLinkBuilder implements ModelLinkBuilder<TagDto> {

    private static final String ALL_TAGS = "tags";
    private static final String CURRENT_TAG = "self";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final TagSearchCriteria defaultRequestBody = TagSearchCriteria.getDefaultTagRequestBody();

    @Override
    public void linkToModel(EntityModel<TagDto> modelDto) {
        try {
            modelDto.add(linkTo(methodOn(TagController.class).getTag(
                    Objects.requireNonNull(modelDto.getContent()).getId())).withRel(CURRENT_TAG));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToModelPage(CollectionModel<EntityModel<TagDto>> collectionModel,
                                int page, int size, SortType sortType, SortBy sortBy) {
        try {
            collectionModel.add(getLinkToTagsPage(page, size, ALL_TAGS, sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToFirstModelPage(EntityModel<TagDto> tagDto, SortType sortType, SortBy sortBy) {
        try {
            tagDto.add(getLinkToTagsPage(DEFAULT_PAGE, DEFAULT_SIZE, ALL_TAGS, sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToNextModelPage(CollectionModel<EntityModel<TagDto>> collectionModel,
                                    int page, int size, SortType sortType, SortBy sortBy) {
        try {
            collectionModel.add(getLinkToTagsPage(page + 1, size, "next", sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToPrevModelPage(CollectionModel<EntityModel<TagDto>> collectionModel,
                                    int page, int size, SortType sortType, SortBy sortBy) {
        try {
            collectionModel.add(getLinkToTagsPage(page - 1, size, "prev", sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    @Override
    public void linkToLastModelPage(CollectionModel<EntityModel<TagDto>> collectionModel,
                                    int lastPage, int size, SortType sortType, SortBy sortBy) {
        try {
            collectionModel.add(getLinkToTagsPage(lastPage, size, "last", sortType, sortBy));
        } catch (ServiceException ignored) {
        }
    }

    private Link getLinkToTagsPage(int page, int size, String rel, SortType sortType, SortBy sortBy)
            throws ServiceException {
        return linkTo(methodOn(TagController.class).getTags(defaultRequestBody, page, size,
                sortType, sortBy)).withRel(rel);
    }
}
