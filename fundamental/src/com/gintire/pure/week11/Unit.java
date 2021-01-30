package com.gintire.pure.week11;

/**
 * Project: spring-boot-test
 * Package: com.gintire.pure.week11
 * <p>
 *
 * @author: jin36
 * @version: Date: 2021-01-30
 * Time: 오후 8:54
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class Unit {
    int x, y;
    Direction dir;

    public Unit() {
        this.x = 0;
        this.y = 0;
        this.dir = Direction.EAST;
    }

    public Unit(Direction dir) {
        this.x = 0;
        this.y = 0;
        this.dir = dir;
    }

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
        this.dir = Direction.EAST;
    }

    public Unit(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
}
