package com.lei.library;


import com.lei.library.bean.BaseResp;

public class Util {
    public Util() {
    }

    public static BaseResp createErrorResp(String status) {
        BaseResp baseResp = new BaseResp();
        baseResp.setCode(status);
        return baseResp;
    }

    public static boolean isIntercepted(BaseResp resp) {
        return RetrofitManager.apiStatusInterceptor != null && RetrofitManager.apiStatusInterceptor.intercept(resp);
    }
}
