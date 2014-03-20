package com.alberta.hita.model;

/**
 * Description.
 * <p/>
 * Date: 3/13/14
 *
 * @version 1.0
 */
public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}
