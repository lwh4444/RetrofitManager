package com.lei.library;


import com.lei.retrofit_manager.bean.BaseResp;

import retrofit2.Call;

public class SimpleRetrofitCallback<T> implements AdvancedRetrofitCallback<T> {
    public SimpleRetrofitCallback() {
    }

    public void onSuccess(Call<T> call, T resp) {
    }

    public void onError(Call<T> call, String errorCode, String msg) {
    }

    public void onError(Call<T> call, BaseResp baseResp) {
    }

    public void onIntercepted(Call<T> call, BaseResp resp) {
    }

    public void onEnd(Call<T> call) {
    }

    public void onCanceled() {
    }
}