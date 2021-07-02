package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.manager.impl.OrderManagerImpl;
import com.epam.esm.dao.request.OrderSearchCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditDisjunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final OrderManagerImpl persistenceManager;

    private static final String GET_ORDER_BY_USER_ID =
            "SELECT o FROM Order o WHERE o.user.id=:userId ";
    private static final String GET_ALL_ORDERS = "SELECT o FROM Order o ";
    private static final String GET_ORDER_COUNT = "SELECT count(o.id) FROM Order o ";


    @Autowired
    public OrderDaoImpl(OrderManagerImpl persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    @Override
    public List<Order> getByUserId(
            int userId, OrderSearchCriteria searchCriteria, int page, int size) throws NoResultException {
        List<Order> orders = getNotAuditedOrdersByUserId(userId, searchCriteria, page, size);
        List<GiftCertificate> giftCertificateList = getFirstVersionOfOrders(orders);

        orders.forEach(o -> {
            List<GiftCertificate> temp = new ArrayList<>(o.getGiftCertificateList());
            giftCertificateList.forEach(c -> temp.replaceAll(t -> t.getId() == c.getId() ? c : t));
            o.setGiftCertificateList(new HashSet<>(temp));
        });

        return orders;
    }

    private List<Order> getNotAuditedOrdersByUserId(int id, OrderSearchCriteria searchCriteria, int page, int size) {
        String query = GET_ORDER_BY_USER_ID +
                "order by " + searchCriteria.getSortBy() + " " + searchCriteria.getSortType();
        TypedQuery<Order> typedQuery = entityManager.createQuery(query, Order.class);
        typedQuery.setParameter("userId", (long)id);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    private List<GiftCertificate> getFirstVersionOfOrders(List<Order> orders) {
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager)
                .createQuery().forRevisionsOfEntity(GiftCertificate.class, true, true);
        auditQuery.add(AuditEntity.revisionNumber().minimize().computeAggregationInInstanceContext());

        orders.forEach(o -> {
            AuditDisjunction disjunction = AuditEntity.disjunction();
            o.getGiftCertificateList().forEach(c -> auditQuery.add(disjunction.add(AuditEntity.id().eq(c.getId()))));
        });

        return auditQuery.getResultList();
    }

    @Override
    public Order getById(long orderId) {
        return persistenceManager.getModelById(orderId);
    }

    @Override
    public List<Order> getAllByPage(OrderSearchCriteria searchCriteria, int page, int size) {
        return persistenceManager.getAllModelsByPage(
                GET_ALL_ORDERS, page, size, searchCriteria.getSortType(), searchCriteria.getSortBy());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceManager.getLastPage(GET_ORDER_COUNT, size);
    }

    @Override
    public Order create(Order order) {
        return persistenceManager.add(order);
    }

    @Override
    public void delete(long orderId) {
        Order order = persistenceManager.getModelById(orderId);
        if (order == null) {
            throw new NoResultException("Failed to find order to delete by id: " + orderId);
        }
        persistenceManager.delete(orderId);
    }
}
