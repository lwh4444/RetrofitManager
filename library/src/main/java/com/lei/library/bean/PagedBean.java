package com.lei.library.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PagedBean<T> {
    @SerializedName(
            value = "pageResult",
            alternate = {"pageInfo"}
    )
    private PageResult pageResult;
    private List<T> resultList;

    public PagedBean() {
    }

    public PageResult getPageResult() {
        return this.pageResult;
    }

    public void setPageResult(PageResult pageResult) {
        this.pageResult = pageResult;
    }

    public List<T> getResultList() {
        return this.resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}

