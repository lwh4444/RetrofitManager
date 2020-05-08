package com.lei.library.bean;

import java.util.List;

public class SimpleListResp<T> extends BaseResp {
    private List<T> data;

    public SimpleListResp() {
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

