package com.tokopedia.seller.gmsubscribe.data.source.product.cloud;

import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.api.GoldMerchantApi;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GMSubscribeProductCloud {

    private final Retrofit retrofit;

    public GMSubscribeProductCloud(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<String> getProduct() {
        return getApi().getGoldMerchantProductList();
    }

    public GoldMerchantApi getApi() {
        return retrofit.create(GoldMerchantApi.class);
    }
}
