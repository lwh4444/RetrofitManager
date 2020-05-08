package com.lei.library;


import com.lei.retrofit_manager.bean.BaseResp;

import retrofit2.Call;

public interface AdvancedRetrofitCallback<T> {
    void onSuccess(Call<T> var1, T var2);

    void onError(Call<T> var1, String var2, String var3);

    void onError(Call<T> var1, BaseResp var2);

    void onIntercepted(Call<T> var1, BaseResp var2);

    void onEnd(Call<T> var1);

    void onCanceled();
}