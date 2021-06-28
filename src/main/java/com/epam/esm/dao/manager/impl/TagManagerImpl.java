package com.epam.esm.dao.manager.impl;

import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagManagerImpl extends PersistenceManagerImpl<Tag> {

    public TagManagerImpl() {
        super.setType(Tag.class);
    }
}
