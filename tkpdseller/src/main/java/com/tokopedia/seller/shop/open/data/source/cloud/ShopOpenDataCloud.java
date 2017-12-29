package com.tokopedia.seller.shop.open.data.source.cloud;

import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCheckDomainName;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCheckShopName;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.ResponseReserveDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopOpenDataCloud {
    private final TomeApi api;

    @Inject
    public ShopOpenDataCloud(TomeApi api) {
        this.api = api;
    }

    public Observable<Boolean> checkDomainName(String domainName) {
        return api.getDomainCheck(domainName).map(new Func1<Response<ResponseCheckDomainName>, Boolean>() {
            @Override
            public Boolean call(Response<ResponseCheckDomainName> responseCheckDomainResponse) {
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
        return api.getShopCheck(shopName).map(new Func1<Response<ResponseCheckShopName>, Boolean>() {
            @Override
            public Boolean call(Response<ResponseCheckShopName> responseCheckShopResponse) {
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

    public Observable<ResponseReserveDomain> reserveShopNameDomain(String shopName, String shopDomainName) {
        return api.reserveDomain(shopName, shopDomainName).map(new Func1<Response<ResponseReserveDomain>, ResponseReserveDomain>() {
            @Override
            public ResponseReserveDomain call(Response<ResponseReserveDomain> responseReserveDomainResponse) {
                if (responseReserveDomainResponse.isSuccessful()
                        && responseReserveDomainResponse.body() != null) {
                    return responseReserveDomainResponse.body();
                } else {
                    throw null;
                }
            }
        });
    }

}
