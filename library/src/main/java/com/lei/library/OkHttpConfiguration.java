package com.lei.library;

import java.util.List;

import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;

public interface OkHttpConfiguration {
    List<Interceptor> interceptors();

    int readTimeoutSeconds();

    int writeTimeoutSeconds();

    int connectTimeoutSeconds();

    X509TrustManager trustManager();
}
