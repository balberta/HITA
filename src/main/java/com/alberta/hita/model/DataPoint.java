package com.alberta.hita.model;

/**
 * Description.
 * <p/>
 * Date: 3/19/14
 *
 * @author <a href="mailto:brian.m.alberta@jpmchase.com">v535083</a>
 * @version 1.0
 */
public class DataPoint<T> {

    private String key;
    private T count;

    public DataPoint() {
    }

    public DataPoint(String key, T count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getCount() {
        return count;
    }

    public void setCount(T count) {
        this.count = count;
    }
}
