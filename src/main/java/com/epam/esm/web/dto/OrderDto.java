package com.epam.esm.web.dto;

import com.epam.esm.model.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderDto extends RepresentationModel<OrderDto> {

    private int id;
    private double totalCost;
    private ZonedDateTime createDate;
    private Set<EntityModel<GiftCertificateDto>> giftCertificateList = new HashSet<>();

    public static OrderDto of(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTotalCost(order.getTotalCost());
        orderDto.setCreateDate(order.getCreateDate());
        Set<EntityModel<GiftCertificateDto>> giftCertificateDto = new HashSet<>();
        order.getGiftCertificateList().forEach(g ->
                giftCertificateDto.add(EntityModel.of(GiftCertificateDto.of(g))));
        orderDto.setGiftCertificateList(giftCertificateDto);

        return orderDto;
    }

    public static List<OrderDto> of(List<Order> orders) {
        return orders.stream().map(OrderDto::of).collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Set<EntityModel<GiftCertificateDto>> getGiftCertificateList() {
        return giftCertificateList;
    }

    public void setGiftCertificateList(Set<EntityModel<GiftCertificateDto>> giftCertificateList) {
        this.giftCertificateList = giftCertificateList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderDto orderDto = (OrderDto) o;
        return id == orderDto.id && Double.compare(orderDto.totalCost, totalCost) == 0 && createDate.equals(orderDto.createDate)
                && giftCertificateList.equals(orderDto.giftCertificateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, totalCost, createDate, giftCertificateList);
    }
}
