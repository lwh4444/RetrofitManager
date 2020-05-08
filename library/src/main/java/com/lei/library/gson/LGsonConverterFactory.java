package com.lei.library.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class LGsonConverterFactory extends Converter.Factory {
    private final Gson gson;
    private final Class baseClazz;

    public static LGsonConverterFactory create(Class baseClazz) {
        return create(new Gson(), baseClazz);
    }

    public static LGsonConverterFactory create(Gson gson, Class baseClazz) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        } else {
            return new LGsonConverterFactory(gson, baseClazz);
        }
    }

    private LGsonConverterFactory(Gson gson, Class baseClazz) {
        this.gson = gson;
        this.baseClazz = baseClazz;
    }

    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
        return new LGsonResponseBodyConverter(this.gson, adapter, this.baseClazz);
    }

    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
        return new LGsonRequestBodyConverter(this.gson, adapter);
    }
}
