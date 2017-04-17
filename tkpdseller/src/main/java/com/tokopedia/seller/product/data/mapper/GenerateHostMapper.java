package com.tokopedia.seller.product.data.mapper;


import com.tokopedia.seller.product.data.source.cloud.model.GenerateHost;
import com.tokopedia.seller.product.data.source.cloud.model.GenerateHostModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class GenerateHostMapper implements Func1<Response<GenerateHostModel>, Observable<? extends GenerateHost>> {

    @Inject
    public GenerateHostMapper() {
    }

    @Override
    public Observable<? extends GenerateHost> call(Response<GenerateHostModel> generateHostModelResponse) {
        if (generateHostModelResponse.isSuccessful() && generateHostModelResponse.body() != null
                && generateHostModelResponse.body().getData() != null
                && generateHostModelResponse.body().getData().getGenerateHost() != null) {
            return Observable.just(generateHostModelResponse.body().getData().getGenerateHost());
        }else{
            throw new RuntimeException("generate host null");
        }
    }
}
