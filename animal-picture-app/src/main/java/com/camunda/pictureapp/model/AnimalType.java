package com.camunda.pictureapp.model;

import lombok.Getter;

@Getter
public enum AnimalType {

    CAT("cat"),
    DOG("dog"),
    BEAR("bear");

    private final String type;

    AnimalType(String type) {
        this.type = type;
    }
}
