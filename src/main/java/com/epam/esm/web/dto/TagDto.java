package com.epam.esm.web.dto;

import com.epam.esm.model.Tag;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TagDto extends RepresentationModel<TagDto> {

    private int id;
    private String name;

    public static List<TagDto> of(List<Tag> tags) {
        return tags.stream().map(TagDto::of).collect(Collectors.toList());
    }

    public static TagDto of(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TagDto tagDto = (TagDto) o;
        return id == tagDto.id && name.equals(tagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name);
    }
}
