package com.lei.library.exception;


import com.lei.library.bean.BaseResp;

public class ResultInterceptedException extends Exception {
    private final BaseResp result;

    public ResultInterceptedException(BaseResp result) {
        this.result = result;
    }

    public BaseResp getResult() {
        return this.result;
    }
}
