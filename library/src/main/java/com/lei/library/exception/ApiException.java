package com.lei.library.exception;

import com.lei.retrofit_manager.bean.BaseResp;

public class ApiException extends Exception {
    private final BaseResp resp;

    public ApiException(BaseResp resp) {
        this.resp = resp;
    }

    public BaseResp getResp() {
        return this.resp;
    }
}

