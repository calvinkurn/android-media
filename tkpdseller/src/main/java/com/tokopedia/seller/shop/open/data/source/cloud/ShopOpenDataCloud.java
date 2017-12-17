package com.tokopedia.seller.shop.open.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseCheckDomain;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseCheckShop;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopOpenDataCloud {
    private final TomeApi api;
    private final Context context;

    @Inject
    public ShopOpenDataCloud(TomeApi api, @ApplicationContext Context context) {
        this.api = api;
        this.context = context;
    }

    public Observable<Boolean> checkDomainName(String domainName) {
        return api.getDomainCheck(domainName).map(new Func1<Response<ResponseCheckDomain>, Boolean>() {
            @Override
            public Boolean call(Response<ResponseCheckDomain> responseCheckDomainResponse) {
                if (responseCheckDomainResponse.isSuccessful()
                        && responseCheckDomainResponse.body() != null) {
                    return "1".equals( responseCheckDomainResponse.body().getDomainStatus());
                } else {
                    throw null;
                }
            }
        });
    }

    public Observable<Boolean> checkShopName(String shopName) {
        return api.getShopCheck(shopName).map(new Func1<Response<ResponseCheckShop>, Boolean>() {
            @Override
            public Boolean call(Response<ResponseCheckShop> responseCheckShopResponse) {
                if (responseCheckShopResponse.isSuccessful()
                        && responseCheckShopResponse.body() != null) {
                    return "1".equals( responseCheckShopResponse.body().getShopNameStatus());
                } else {
                    throw null;
                }
            }
        });
    }

    public Observable<ResponseIsReserveDomain> isReserveDomainResponseObservable() {
        return api.isReserveDomain().map(new Func1<Response<ResponseIsReserveDomain>, ResponseIsReserveDomain>() {
            @Override
            public ResponseIsReserveDomain call(Response<ResponseIsReserveDomain> responseCheckShopResponse) {
                if (responseCheckShopResponse.isSuccessful()
                        && responseCheckShopResponse.body() != null) {
                    return responseCheckShopResponse.body();
                } else {
                    throw null;
                }
            }
        });
    }

}
