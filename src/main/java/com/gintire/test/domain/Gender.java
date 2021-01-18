package com.gintire.test.domain;

public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    INTERSEX("자유주의자");

    final private String name;
    private Gender(String name) {
        this.name = name;
    }
    private String getName() {
        return name;
    }
}
