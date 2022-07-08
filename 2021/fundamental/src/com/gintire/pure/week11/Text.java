package com.gintire.pure.week11;

import java.util.Spliterator;
import java.util.stream.IntStream;

/**
 * Project: spring-boot-test
 * Package: com.gintire.pure.week11
 * <p>
 *
 * @author: jin36
 * @version: Date: 2021-01-30
 * Time: 오후 10:19
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class Text {
    public static final int STYLE_BOLD = 1 << 0;    //1
    public static final int STYLE_ITALIC = 1 << 1;    //2
    public static final int STYLE_UNDERLINE = 1 << 2;    //4
    public static final int STYLE_STRIKETHROUGH = 1 << 3;    //8

    // 이 메서드의 인자는 STYLE_ 상수를 비트별 (bitwise) OR 한 값이거나 0,
    public void applyStyles (int styles) {
        //
    }
}
