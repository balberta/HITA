package com.alberta.hita.model;

/**
 * Description.
 * <p/>
 * Date: 10/7/13
 *
 */
public class Task {

    private int uuid;
    private String  name;
    private String type;
    private String status;
    private String description;
    private String updateTime;
    private String submitTime;

    public Task(int uuid, String name, String type, String status, String description) {
        this.uuid = uuid;
        this.name = name;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public Task() {
    }

    public Task(String name, String type, String status, String description) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public Task(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
