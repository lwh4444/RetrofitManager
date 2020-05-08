package com.lei.library;


import com.lei.library.bean.BaseResp;
import com.lei.library.exception.ApiException;
import com.lei.library.exception.ResultInterceptedException;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvancedRetrofitHelper {
    public static final String SUCCESS = "100";
    public static final String FAILURE = "-1";
    private static WeakHashMap<Object, List<Call>> callMap = new WeakHashMap();
    private static WeakHashMap<Object, CompositeDisposable> disposableMap = new WeakHashMap();

    public AdvancedRetrofitHelper() {
    }

    public static void addCall(Object key, Call call) {
        List<Call> calls = (List)callMap.get(key);
        if (calls == null) {
            calls = new ArrayList();
            callMap.put(key, calls);
        }

        if (!((List)calls).contains(call)) {
            ((List)calls).add(call);
        }

    }

    public static void addDisposable(Object key, Disposable disposable) {
        CompositeDisposable compositeDisposable = (CompositeDisposable)disposableMap.get(key);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            disposableMap.put(key, compositeDisposable);
        }

        compositeDisposable.add(disposable);
    }

    public static void cancelCalls(Object key) {
        List<Call> calls = (List)callMap.get(key);
        if (calls != null) {
            Iterator var2 = calls.iterator();

            while(var2.hasNext()) {
                Call call = (Call)var2.next();
                if (call != null && !call.isCanceled()) {
                    call.cancel();
                }
            }

            callMap.remove(key);
        }

    }

    public static void disposeDisposables(Object key) {
        CompositeDisposable compositeDisposable = (CompositeDisposable)disposableMap.get(key);
        if (compositeDisposable != null) {
            if (!compositeDisposable.isDisposed()) {
                compositeDisposable.dispose();
            }

            disposableMap.remove(key);
        }

    }

    public static void releaseResourcesFor(Object key) {
        cancelCalls(key);
        disposeDisposables(key);
    }

    public static void remove(Object key) {
        callMap.remove(key);
        disposableMap.remove(key);
    }

    public static <T extends BaseResp> void enqueue(Object key, Call<T> call, AdvancedRetrofitCallback<T> callback) {
        enqueue(key, call, callback, true);
    }

    public static <T extends BaseResp> void enqueue(Object key, Call<T> call, AdvancedRetrofitCallback<T> callback, boolean shouldAddCall) {
        if (shouldAddCall) {
            addCall(key, call);
        }

        enqueueCall(call, callback);
    }

    private static <T extends BaseResp> void enqueueCall(Call<T> call, final AdvancedRetrofitCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            public void onResponse(Call<T> call, Response<T> response) {
                if (!call.isCanceled()) {
                    if (response.isSuccessful()) {
                        T body = response.body();
                        String statusx;
                        if (body != null) {
                            statusx = body.getStatus();
                            String msg = body.getMsg();
                            if (Util.isIntercepted(body)) {
                                if (callback != null) {
                                    callback.onIntercepted(call, body);
                                }
                            } else if ("100".equals(statusx)) {
                                if (callback != null) {
                                    callback.onSuccess(call, body);
                                }
                            } else if (callback != null) {
                                callback.onError(call, statusx, msg);
                                callback.onError(call, body);
                            }
                        } else {
                            statusx = String.valueOf(response.code());
                            AdvancedRetrofitHelper.onCallError(call, statusx, callback);
                        }
                    } else {
                        String status = String.valueOf(response.code());
                        AdvancedRetrofitHelper.onCallError(call, status, callback);
                    }

                    if (callback != null) {
                        callback.onEnd(call);
                    }
                } else if (callback != null) {
                    callback.onCanceled();
                }

            }

            public void onFailure(Call<T> call, Throwable t) {
                if (!call.isCanceled()) {
                    String status = "-1";
                    if (t instanceof ConnectException) {
                        status = "-2";
                    }

                    AdvancedRetrofitHelper.onCallError(call, status, callback);
                    if (callback != null) {
                        callback.onEnd(call);
                    }
                } else if (callback != null) {
                    callback.onCanceled();
                }

            }
        });
    }

    private static <T extends BaseResp> void onCallError(Call<T> call, String status, AdvancedRetrofitCallback<T> callback) {
        BaseResp errorResp = Util.createErrorResp(status);
        if (Util.isIntercepted(errorResp)) {
            if (callback != null) {
                callback.onIntercepted(call, errorResp);
            }
        } else if (callback != null) {
            callback.onError(call, status, (String)null);
            callback.onError(call, errorResp);
        }

    }

    public static <T extends BaseResp> ObservableTransformer<T, T> rxObservableTransformer(Object key) {
        return rxObservableTransformer(key, true);
    }

    public static <T extends BaseResp> ObservableTransformer<T, T> rxObservableTransformer(final Object key, final boolean shouldAddDisposable) {
        return new ObservableTransformer<T, T>() {
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        if (shouldAddDisposable) {
                            AdvancedRetrofitHelper.addDisposable(key, disposable);
                        }

                    }
                }).map(new Function<T, T>() {
                    public T apply(T t) throws Exception {
                        if (Util.isIntercepted(t)) {
                            throw new ResultInterceptedException(t);
                        } else {
                            return t;
                        }
                    }
                }).map(new Function<T, T>() {
                    public T apply(@NonNull T t) throws Exception {
                        String status = t.getStatus();
                        if (!"100".equals(status)) {
                            throw new ApiException(t);
                        } else {
                            return t;
                        }
                    }
                });
            }
        };
    }
}
