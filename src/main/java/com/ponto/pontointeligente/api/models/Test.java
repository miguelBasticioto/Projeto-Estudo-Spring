package com.ponto.pontointeligente.api.models;

import java.io.Serializable;

public class Test implements Serializable {

    private int userId;
    private int id;
    private String title;
    private Boolean body;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getBody() {
        return body;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(Boolean body) {
        this.body = body;
    }
}