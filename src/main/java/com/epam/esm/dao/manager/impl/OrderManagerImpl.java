package com.epam.esm.dao.manager.impl;

import com.epam.esm.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderManagerImpl extends PersistenceManagerImpl<Order> {

    public OrderManagerImpl() {
        super.setType(Order.class);
    }
}
