package com.epam.esm.model;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
@Audited
public class Order extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 0, message = "cost must be greater than 0 or = 0")
    @Column(name = "Cost")
    private double totalCost;

    @Column(name = "CreateDate")
    private LocalDateTime createDate;

    @NotNull(message = "order must have user")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable=false)
    private User user;

    @NotNull(message = "order must have at least 1 certificate")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "OrderCertificate",
            joinColumns = @JoinColumn(name = "orderId"),
            inverseJoinColumns = @JoinColumn(name = "certificateId")
    )
    private Set<GiftCertificate> giftCertificateList = new HashSet<>();

    public Order() {
    }

    public Order(int id, double totalCost, LocalDateTime createDate, User user, Set<GiftCertificate> giftCertificateList) {
        this.id = id;
        this.totalCost = totalCost;
        this.createDate = createDate;
        this.user = user;
        this.giftCertificateList = giftCertificateList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double cost) {
        this.totalCost = cost;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<GiftCertificate> getGiftCertificateList() {
        return giftCertificateList;
    }

    public void setGiftCertificateList(Set<GiftCertificate> giftCertificateList) {
        this.giftCertificateList = giftCertificateList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Double.compare(order.totalCost, totalCost) == 0 && createDate.equals(order.createDate) && user.equals(order.user) && giftCertificateList.equals(order.giftCertificateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalCost, createDate, giftCertificateList);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", cost=" + totalCost +
                ", createDate=" + createDate +
                ", user=" + user +
                ", giftCertificateList=" + giftCertificateList +
                '}';
    }
}
