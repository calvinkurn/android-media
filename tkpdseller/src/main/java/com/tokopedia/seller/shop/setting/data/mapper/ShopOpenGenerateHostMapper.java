package com.tokopedia.seller.shop.setting.data.mapper;

import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class ShopOpenGenerateHostMapper implements Func1<Response<GenerateHostModel>, Observable<? extends GenerateHostModel.GenerateHost>> {

    @Inject
    public ShopOpenGenerateHostMapper() {
    }

    @Override
    public Observable<? extends GenerateHostModel.GenerateHost> call(Response<GenerateHostModel> generateHostModelResponse) {
        if (generateHostModelResponse.isSuccessful() && generateHostModelResponse.body() != null
                && generateHostModelResponse.body().getData() != null
                && generateHostModelResponse.body().getData().getGenerateHost() != null) {
            return Observable.just(generateHostModelResponse.body().getData().getGenerateHost());
        } else {
            return null;
        }
    }
}
