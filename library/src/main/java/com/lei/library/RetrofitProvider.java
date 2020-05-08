package com.lei.library;

public interface RetrofitProvider {
    String provideBaseUrl();

    OkHttpConfiguration provideOkHttpConfig();

    ApiStatusInterceptor provideApiStatusInterceptor();
}
