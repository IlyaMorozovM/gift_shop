package com.epam.esm.dao.manager.impl;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateManagerImpl extends PersistenceManagerImpl<GiftCertificate> {

    public GiftCertificateManagerImpl() {
        super.setType(GiftCertificate.class);
    }
}
