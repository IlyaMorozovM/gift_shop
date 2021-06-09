package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.Tag;
import com.google.common.base.CaseFormat;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@EntityScan(basePackages = "com.epam.esm.model")
@ComponentScan(basePackages = "com.epam.esm.dao.service.impl")
public class GiftCertificateDaoImpl implements GiftCertificateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private static final boolean ACTIVE_CERTIFICATE = true;
    private static final boolean DELETED_CERTIFICATE = false;

    private final PersistenceService<GiftCertificate> persistenceService;

    private static final String GET_CERTIFICATE_BY_NAME =
            "SELECT g FROM GiftCertificate g WHERE g.name=:name AND g.isActive=true ";
    private static final String GET_CERTIFICATE_COUNT =
            "SELECT count(g.id) FROM GiftCertificate g WHERE g.isActive=true ";

    @Autowired
    public GiftCertificateDaoImpl(PersistenceService<GiftCertificate> persistenceService) {
        this.persistenceService = persistenceService;
    }

    @PostConstruct
    private void init() {
        persistenceService.setType(GiftCertificate.class);
    }

    @Override
    public GiftCertificate getByName(String name) throws NoResultException {
        return persistenceService.getModelByName(GET_CERTIFICATE_BY_NAME, name);
    }

    @Override
    public GiftCertificate getById(int certificateId) {
        return persistenceService.getModelById(certificateId);
    }

    @Override
    public List<GiftCertificate> getByRequestBody(
            CertificateSearchCriteria searchCriteria, int page, int size) {
        List<GiftCertificate> giftCertificates = getGiftCertificatesFromQuery(searchCriteria, page, size);
        removeDeletedTags(giftCertificates);
        
        return giftCertificates;
    }

    private List<GiftCertificate> getGiftCertificatesFromQuery(
            CertificateSearchCriteria searchCriteria, int page, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);

        query.select(root).distinct(true);
        buildQuery(root, query, searchCriteria);

        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, searchCriteria.getSortBy().toString());
        if (searchCriteria.getSortType().equals(SortType.ASC)) {
            query.orderBy(builder.asc(root.get(CaseFormat.LOWER_UNDERSCORE
                    .to(CaseFormat.LOWER_CAMEL, searchCriteria.getSortBy().toString()))));
        } else {
            query.orderBy(builder.desc(root.get(CaseFormat.LOWER_UNDERSCORE
                    .to(CaseFormat.LOWER_CAMEL, searchCriteria.getSortBy().toString()))));
        }

        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    private <T extends AbstractQuery<GiftCertificate>> void buildQuery(
            Root<GiftCertificate> root, T query, CertificateSearchCriteria searchCriteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();

        if (searchCriteria.getContent() != null) {
            Predicate predicateForDescription =
                    builder.like(root.get("name"), "%" + searchCriteria.getContent() + "%");
            predicates.add(predicateForDescription);
            predicateForDescription =
                    builder.like(root.get("description"), "%" + searchCriteria.getContent() + "%");
            predicates.add(predicateForDescription);
        }

        if (searchCriteria.getTagNames() != null) {
            Join<GiftCertificate, Tag> tags = root.join("tags");
            CriteriaBuilder.In<String> inTags = builder.in(tags.get("name"));
            for (String tagName : searchCriteria.getTagNames()) {
                inTags.value(tagName);
            }
            predicates.add(inTags);
            query.groupBy(root.get("id"));
            query.having(builder.equal(builder.count(root.get("id")), searchCriteria.getTagNames().size()));
        }

        Predicate[] predArray = new Predicate[predicates.size()];
        predicates.toArray(predArray);
        query.where(predArray);
    }

    private void removeDeletedTags(List<GiftCertificate> giftCertificates) {
        giftCertificates.forEach(g -> g.getTags().removeIf(tag -> !tag.isActive()));
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_CERTIFICATE_COUNT, size);
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws PersistenceException {
        giftCertificate.setActive(ACTIVE_CERTIFICATE);
        return persistenceService.add(giftCertificate);
    }

    @Override
    public void delete(int certificateId) {
        GiftCertificate giftCertificate = persistenceService.getModelById(certificateId);
        if (giftCertificate == null) {
            throw new NoResultException("Failed to find certificate to delete by id: " + certificateId);
        }
        giftCertificate.setActive(DELETED_CERTIFICATE);
        update(giftCertificate);
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        giftCertificate.setActive(ACTIVE_CERTIFICATE);
        return persistenceService.update(giftCertificate);
    }
}
