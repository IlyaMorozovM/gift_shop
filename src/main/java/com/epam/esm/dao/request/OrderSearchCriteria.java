package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class OrderSearchCriteria extends SortingSearchCriteria {

    private static final SortBy DEFAULT_SORT_BY = SortBy.COST;
    private static final SortType DEFAULT_SORT_TYPE = SortType.ASC;

    public static OrderSearchCriteria getDefaultOrderRequestBody() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setSortBy(DEFAULT_SORT_BY);
        orderSearchCriteria.setSortType(DEFAULT_SORT_TYPE);

        return orderSearchCriteria;
    }
}
