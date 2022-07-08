package com.gintire.pure.week12;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Project: spring-boot-test
 * Package: com.gintire.pure.week12
 * <p>
 *
 * @author: jin36
 * @version: Date: 2021-02-06
 * Time: 오후 11:48
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        /*Annotation[] annotations = ChildTest.class
                .getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation.toString());
        }*/

        Field[] fields = ChildTest.class
                .getFields();
        for (Field field : fields) {
            System.out.println(field.toString());
        }
    }
}
