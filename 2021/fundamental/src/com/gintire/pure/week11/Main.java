package com.gintire.pure.week11;

import java.lang.annotation.ElementType;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Project: spring-boot-test
 * Package: com.gintire.pure.week11
 * <p>
 *
 * @author: jin36
 * @version: Date: 2021-01-30
 * Time: 오후 8:44
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(Card.Kind.CLOVER.equals(Card.Value.TWO));
        System.out.println(Card.Kind.CLOVER);
        System.out.println(Card.Value.TWO);

        System.out.printf("Gender 한글 : %s \n", Gender.MALE.getKorean());
        System.out.printf("Gender 기본 : %s \n", Gender.MALE);

        System.out.printf("Gender Male name : %s \n", Gender.MALE.name());
        System.out.printf("Gender FeMale name : %s \n", Gender.FEMALE.name());

        System.out.printf("Direction EAST getDeclaringClass : %s \n", Direction.EAST.getDeclaringClass());
        System.out.printf("Direction EAST name : %s \n",Direction.EAST.name());
        System.out.printf("Direction EAST ordinal : %s \n",Direction.EAST.ordinal());
        // static method
        System.out.printf("Direction EAST valueOf (static) : %s \n",Direction.valueOf(Direction.class, "EAST"));

        // implicit methods
        // valueOf() - 상수의 이름으로 문자열 상수에 대한 참조를 얻을 수 있다.
        Direction d= Direction.valueOf("WEST");
        System.out.printf("Direction valueOf : %s ", d);  // WEST
        System.out.println(Direction.WEST==Direction.valueOf("WEST")); // TRUE
        // value()
        System.out.println("###############################################");
        System.out.println("##############Direction.values()###############");
        System.out.println("###############################################");
        Arrays.stream(Direction.values())
                .forEach(direction -> System.out.println(direction.name()));

        // EnumSet Test
        Text text = new Text();
        // STYLE_BOLD = 1 << 0;
        // STYLE_ITALLIC = 1 << 1;
        // 0001 | 0010 -> 0011
        text.applyStyles(Text.STYLE_BOLD | Text.STYLE_ITALIC);

        TestEmum testEmum = new TestEmum();
        testEmum.applyStyles(EnumSet.of(TestEmum.Style.BOLD, TestEmum.Style.ITALIC));

        Herb[] garden = new Herb[3];
        garden[0] = new Herb("Basil", Herb.Type.ANNUAL);
        garden[1] = new Herb("Lovage", Herb.Type.PERENNIAL);
        garden[2] = new Herb("Angelica", Herb.Type.BIENNIAL);

        Map<Herb.Type, Set<Herb>> herbsByType = new EnumMap<Herb.Type, Set<Herb>>(Herb.Type.class);
        for(Herb.Type t : Herb.Type.values()) herbsByType.put(t, new HashSet<>());
        for(Herb h : garden) herbsByType.get(h.type).add(h);
        System.out.println(herbsByType);
    }
}
