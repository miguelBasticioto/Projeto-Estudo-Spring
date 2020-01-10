package com.ponto.pontointeligente.api.response;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {
    private T data;

    private List<String> errors;

    public Response() {

    }

    public T getData() {
        return data;
    }

    public List<String> getErrors() {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }

        return errors;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
