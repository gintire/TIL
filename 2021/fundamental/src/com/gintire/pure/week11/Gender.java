package com.gintire.pure.week11;

/**
 * Project: spring-boot-test
 * Package: com.gintire.pure.week11
 * <p>
 *
 * @author: jin36
 * @version: 21.01
 * Date: 2021-01-30
 * Time: 오후 7:57
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public enum Gender {
    MALE("남자", "MALE"),
    FEMALE("여자", "FEMALE");

    private String korean;
    private String english;

    Gender(String korean, String english) {
        this.korean = korean;
        this.english = english;
    }

    public String getKorean() {
        return korean;
    }

    public String getEnglish() {
        return english;
    }
}
