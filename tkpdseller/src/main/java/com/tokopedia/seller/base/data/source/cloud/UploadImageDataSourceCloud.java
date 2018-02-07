package com.tokopedia.seller.base.data.source.cloud;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class UploadImageDataSourceCloud {

    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient okHttpClient;

    @Inject
    public UploadImageDataSourceCloud(Retrofit.Builder retrofitBuilder, @DefaultAuthWithErrorHandler OkHttpClient okHttpClient) {
        this.retrofitBuilder = retrofitBuilder;
        this.okHttpClient = okHttpClient;
    }

    public Observable<String> uploadImage(Map<String, RequestBody> params, String urlUploadImage) {
        Retrofit retrofit = retrofitBuilder.client(okHttpClient).build();
        return retrofit.create(ImageUploadApi.class)
                .uploadImage(urlUploadImage, params)
                .flatMap(new Func1<Response<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(Response<String> stringResponse) {
                        return Observable.just(stringResponse.body());
                    }
                });
    }
}
