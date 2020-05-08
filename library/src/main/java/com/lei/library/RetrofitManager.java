package com.lei.library;

import android.util.Log;

import com.lei.retrofit_manager.bean.BaseResp;
import com.lei.retrofit_manager.gson.LGsonConverterFactory;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitManager {
    private static final String TAG = "RetrofitManager";
    static final int DEFAULT_CONNECT_TIMEOUT = 30;
    static final int DEFAULT_READ_TIMEOUT = 30;
    static final int DEFAULT_WRITE_TIMEOUT = 30;
    static ApiStatusInterceptor apiStatusInterceptor;
    private static Retrofit retrofit;
    private static OkHttpClient client;

    public RetrofitManager() {
    }

    public static void init(RetrofitProvider provider) {
        if (retrofit != null) {
            Log.w("RetrofitManager", "RetrofitManager already initialized!");
        } else if (provider == null) {
            throw new IllegalArgumentException("RetrofitProvider must NOT be null!");
        } else {
            apiStatusInterceptor = provider.provideApiStatusInterceptor();
            client = buildClient(provider.provideOkHttpConfig());
            retrofit = (new Retrofit.Builder()).baseUrl(provider.provideBaseUrl()).addConverterFactory(LGsonConverterFactory.create(BaseResp.class)).addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())).client(client).build();
        }
    }

    public static OkHttpClient getOkHttpClient() {
        return client;
    }

    private static OkHttpClient buildClient(OkHttpConfiguration config) {
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        if (config != null) {
            List<Interceptor> interceptors = config.interceptors();
            if (interceptors != null) {
                clientBuilder.interceptors().addAll(interceptors);
            }

            int connectTimeoutSeconds = config.connectTimeoutSeconds();
            clientBuilder.connectTimeout((long)connectTimeoutSeconds, TimeUnit.SECONDS);
            int readTimeoutSeconds = config.readTimeoutSeconds();
            clientBuilder.readTimeout((long)readTimeoutSeconds, TimeUnit.SECONDS);
            int writeTimeoutSeconds = config.writeTimeoutSeconds();
            clientBuilder.writeTimeout((long)writeTimeoutSeconds, TimeUnit.SECONDS);
            X509TrustManager trustManager = config.trustManager();
            if (trustManager != null) {
                SSLSocketFactory sslSocketFactory;
                try {
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
                    sslSocketFactory = sslContext.getSocketFactory();
                } catch (GeneralSecurityException var9) {
                    throw new RuntimeException(var9);
                }

                clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
            }
        } else {
            clientBuilder.connectTimeout(30L, TimeUnit.SECONDS);
            clientBuilder.readTimeout(30L, TimeUnit.SECONDS);
            clientBuilder.writeTimeout(30L, TimeUnit.SECONDS);
        }

        return clientBuilder.build();
    }

    public static <T> T createService(Class<T> service) {
        if (retrofit == null) {
            throw new IllegalStateException("RetrofitManager not initialized! Call RetrofitManager.init() in your custom application class!");
        } else {
            return retrofit.create(service);
        }
    }
}
