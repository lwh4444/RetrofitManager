package com.lei.library.rx;


import com.lei.library.Util;
import com.lei.library.bean.BaseResp;
import com.lei.library.exception.ApiException;
import com.lei.library.exception.ResultInterceptedException;

import java.net.ConnectException;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

public abstract class ErrorConsumer implements Consumer<Throwable> {
    public ErrorConsumer() {
    }

    public final void accept(Throwable throwable) throws Exception {
        if (throwable instanceof ResultInterceptedException) {
            ResultInterceptedException interceptedException = (ResultInterceptedException)throwable;
            this.onIntercepted(interceptedException.getResult());
        } else {
            BaseResp resp;
            if (throwable instanceof ApiException) {
                ApiException apiException = (ApiException)throwable;
                resp = apiException.getResp();
            } else if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException)throwable;
                String status = String.valueOf(httpException.code());
                resp = Util.createErrorResp(status);
            } else {
                String status = "-1";
                if (throwable instanceof ConnectException) {
                    status = "-2";
                }

                resp = Util.createErrorResp(status);
            }

            if (Util.isIntercepted(resp)) {
                this.onIntercepted(resp);
            } else {
                this.onError(resp);
            }
        }

    }

    public abstract void onError(BaseResp var1);

    public void onIntercepted(BaseResp result) {
    }
}
