package com.lei.library.bean;
public class SimplePagedListResp<T> extends BaseResp {
    private PagedBean<T> data;

    public SimplePagedListResp() {
    }

    public PagedBean<T> getData() {
        return this.data;
    }

    public void setData(PagedBean<T> data) {
        this.data = data;
    }
}