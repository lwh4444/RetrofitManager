package com.lei.library.gson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class LGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final Class baseBeanClazz;

    LGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Class baseBeanClazz) {
        this.gson = gson;
        this.adapter = adapter;
        this.baseBeanClazz = baseBeanClazz;
    }

    public T convert(ResponseBody value) throws IOException {
        String valueString = null;

        T var4;
        try {
            valueString = value.string();
            T var3 = this.adapter.fromJson(valueString);
            return var3;
        } catch (JsonSyntaxException var10) {
            try {
                var4 = (T)this.gson.fromJson(valueString, this.baseBeanClazz);
            } catch (ClassCastException | JsonSyntaxException var9) {
                throw var10;
            }
        } finally {
            value.close();
        }

        return var4;
    }
}
