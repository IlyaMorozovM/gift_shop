package com.epam.esm.web.hateoas;

import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;

public interface ModelAssembler<T> extends SimpleRepresentationModelAssembler<T> {

    void setRepresentationModel(RepresentationModel representationModel);
    void setModelLinkBuilder(ModelLinkBuilder<T> modelLinkBuilder);
}
