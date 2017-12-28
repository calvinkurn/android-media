package com.tokopedia.loyalty.domain.apiservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class TokoPointRetrofitFactory {

    public static Retrofit.Builder createRetrofitTokoPointConfig(String baseUrl) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(TokoPointResponseConverter.create())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }
}
