package com.lei.library.exception;


import com.lei.library.bean.BaseResp;

public class ApiException extends Exception {
    private final BaseResp resp;

    public ApiException(BaseResp resp) {
        this.resp = resp;
    }

    public BaseResp getResp() {
        return this.resp;
    }
}

