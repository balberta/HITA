package com.alberta.hita.model;

/**
 * Description.
 * <p/>
 * Date: 10/7/13
 *
 * @version 1.0
 */
public enum Status {
    NEW("New"), OPEN("Open"), CLOSED("Closed"), TASK("Task"), ISSUE("Issue");

    private String text;

    Status(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Status fromString(String text) {
        if (text != null) {
            for (Status b : Status.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
