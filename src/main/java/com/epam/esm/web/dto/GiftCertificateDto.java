package com.epam.esm.web.dto;

import com.epam.esm.model.GiftCertificate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    private int id;
    private String name;
    private String description;
    private double price;
    private ZonedDateTime createDate;
    private ZonedDateTime lastUpdateDate;
    private int duration;
    private Set<EntityModel<TagDto>> tags = new HashSet<>();

    public static GiftCertificateDto of(GiftCertificate giftCertificate) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setId(giftCertificate.getId());
        giftCertificateDto.setName(giftCertificate.getName());
        giftCertificateDto.setDescription(giftCertificate.getDescription());
        giftCertificateDto.setPrice(giftCertificate.getPrice());
        giftCertificateDto.setCreateDate(giftCertificate.getCreateDate());
        giftCertificateDto.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        giftCertificateDto.setDuration(giftCertificate.getDuration());
        Set<EntityModel<TagDto>> tagsDto = new HashSet<>();
        giftCertificate.getTags().forEach(t -> tagsDto.add(EntityModel.of(TagDto.of(t))));
        giftCertificateDto.setTags(tagsDto);

        return giftCertificateDto;
    }

    public static List<GiftCertificateDto> of(List<GiftCertificate> certificates) {
        return certificates.stream().map(GiftCertificateDto::of).collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<EntityModel<TagDto>> getTags() {
        return tags;
    }

    public void setTags(Set<EntityModel<TagDto>> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GiftCertificateDto that = (GiftCertificateDto) o;
        return id == that.id && Double.compare(that.price, price) == 0 && duration == that.duration && name.equals(that.name) && description.equals(that.description) && createDate.equals(that.createDate) && lastUpdateDate.equals(that.lastUpdateDate) && tags.equals(that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, price, createDate, lastUpdateDate, duration, tags);
    }
}
