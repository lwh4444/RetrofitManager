package com.lei.library.bean;

public class SimpleResp<T> extends BaseResp {
    private T data;

    public SimpleResp() {
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
