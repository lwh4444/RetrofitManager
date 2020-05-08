package com.lei.library.bean;

public class PageResult implements Cloneable {
    private int curPage;
    private int pageSize;
    private boolean hasMore;
    private int total;
    private int maxSize;

    public PageResult() {
    }

    public int getCurPage() {
        return this.curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasMore() {
        return this.hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Object clone() throws CloneNotSupportedException {
        PageResult pageResult = null;

        try {
            pageResult = (PageResult) super.clone();
        } catch (CloneNotSupportedException var3) {
            var3.printStackTrace();
        }

        return pageResult;
    }
}