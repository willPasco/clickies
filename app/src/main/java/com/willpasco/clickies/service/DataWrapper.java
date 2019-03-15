package com.willpasco.clickies.service;

public class DataWrapper<T> {
    private T data;
    private String errorMessage;

    public DataWrapper(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public boolean checkError() {
        return errorMessage == null;
    }
}
