package com.lei.library;

import java.util.List;

import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;

public class SimpleOkHttpConfiguration {
    public SimpleOkHttpConfiguration() {
    }

    public List<Interceptor> interceptors() {
        return null;
    }

    public int readTimeoutSeconds() {
        return 30;
    }

    public int writeTimeoutSeconds() {
        return 30;
    }

    public int connectTimeoutSeconds() {
        return 30;
    }

    public X509TrustManager trustManager() {
        return null;
    }
}
