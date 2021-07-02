package com.epam.esm.web.hateoas;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import org.springframework.hateoas.PagedModel;

public class RepresentationModel {

    private PagedModel.PageMetadata pageMetadata;
    private SortType sortType;
    private SortBy sortBy;

    public RepresentationModel(PagedModel.PageMetadata pageMetadata, SortType sortType, SortBy sortBy) {
        this.pageMetadata = pageMetadata;
        this.sortType = sortType;
        this.sortBy = sortBy;
    }

    public PagedModel.PageMetadata getPageMetadata() {
        return pageMetadata;
    }

    public void setPageMetadata(PagedModel.PageMetadata pageMetadata) {
        this.pageMetadata = pageMetadata;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }
}
