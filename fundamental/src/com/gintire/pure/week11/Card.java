package com.gintire.pure.week11;

/**
 * Project: spring-boot-test
 * Package: com.gintire.pure.week11
 * <p>
 *
 * @author: jin36
 * @version: Date: 2021-01-30
 * Time: 오후 8:30
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
class Card {
    enum Kind {CLOVER, HEART, DIAMOND, SPACE}

    enum Value {TWO, THREE, FOUR}

    final Kind kind;    // 타입이 Kind임에 유의;
    final Value value;

    public Card(Kind kind, Value value) {
        this.kind = kind;
        this.value = value;
    }

    public Kind getKind() {
        return kind;
    }

    public Value getValue() {
        return value;
    }
}
