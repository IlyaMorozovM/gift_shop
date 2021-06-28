package com.epam.esm.dao.manager.impl;

import com.epam.esm.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserManagerImpl extends PersistenceManagerImpl<User> {

    public UserManagerImpl() {
        super.setType(User.class);
    }

}
