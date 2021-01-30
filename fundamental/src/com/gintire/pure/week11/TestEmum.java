package com.gintire.pure.week11;

import java.util.Set;

/**
 * Project: spring-boot-test
 * Package: com.gintire.pure.week11
 * <p>
 *
 * @author: jin36
 * @version: Date: 2021-01-30
 * Time: 오후 10:51
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class TestEmum {
    public enum Style {
        BOLD, ITALIC, UNDERLINE, STRIKETHROUGH
    }
    // 어떤 Set 객체도 인자로 전달할 수 있으나, EnumSet이 분명 최선
    public void applyStyles(Set<Style> styles) {}
}
