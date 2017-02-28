package com.tokopedia.seller.gmsubscribe.data.source.product.cloud;

import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.api.GoldMerchantApi;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GmServiceModel;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductCloud {

    private final Retrofit retrofit;

    public GmSubscribeProductCloud(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<GmServiceModel> getProduct() {
        return getApi()
                .getGoldMerchantProductList()
                .map(new GetResponse<GmServiceModel>());
    }

    public GoldMerchantApi getApi() {
        return retrofit.create(GoldMerchantApi.class);
    }

    private class GetResponse<T> implements Func1<Response<T>, T> {
        @Override
        public T call(Response<T> response) {
            return response.body();
        }
    }
}
