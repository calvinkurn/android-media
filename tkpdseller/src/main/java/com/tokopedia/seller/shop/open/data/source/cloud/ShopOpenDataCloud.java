package com.tokopedia.seller.shop.open.data.source.cloud;

import com.tokopedia.seller.common.data.mapper.DataResponseMapper;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCheckDomainName;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCheckShopName;
import com.tokopedia.seller.shop.open.data.model.response.ResponseReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopOpenDataCloud {
    private static final String STATUS_AVAILABLE = "1";
    private final TomeApi api;

    @Inject
    public ShopOpenDataCloud(@ShopQualifier TomeApi api) {
        this.api = api;
    }

    public Observable<Boolean> checkDomainName(String domainName) {
        return api.getDomainCheck(domainName)
                .map(new DataResponseMapper<ResponseCheckDomainName>())
                .flatMap(new Func1<ResponseCheckDomainName, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(ResponseCheckDomainName responseCheckDomainName) {
                        if (responseCheckDomainName == null) {
                            throw new RuntimeException();
                        } else {
                            return Observable.just(STATUS_AVAILABLE.equals(responseCheckDomainName.getDomainStatus()));
                        }
                    }
                });
    }

    public Observable<Boolean> checkShopName(String shopName) {
        return api.getShopCheck(shopName).map(new DataResponseMapper<ResponseCheckShopName>())
                .flatMap(new Func1<ResponseCheckShopName, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(ResponseCheckShopName responseCheckShopName) {
                        if (responseCheckShopName == null) {
                            throw new RuntimeException();
                        } else {
                            return Observable.just(STATUS_AVAILABLE.equals(responseCheckShopName.getShopNameStatus()));
                        }
                    }
                });
    }

    public Observable<ResponseIsReserveDomain> isReserveDomainResponseObservable() {
        return api.isReserveDomain()
                .map(new DataResponseMapper<ResponseIsReserveDomain>())
                .flatMap(new Func1<ResponseIsReserveDomain, Observable<ResponseIsReserveDomain>>() {
                    @Override
                    public Observable<ResponseIsReserveDomain> call(ResponseIsReserveDomain responseIsReserveDomain) {
                        if (responseIsReserveDomain == null) {
                            throw new RuntimeException();
                        } else {
                            return Observable.just(responseIsReserveDomain);
                        }
                    }
                });
    }

    public Observable<ResponseReserveDomain> reserveShopNameDomain(String shopName, String shopDomainName) {
        return api.reserveDomain(shopName, shopDomainName)
                .map(new DataResponseMapper<ResponseReserveDomain>())
                .flatMap(new Func1<ResponseReserveDomain, Observable<ResponseReserveDomain>>() {
                    @Override
                    public Observable<ResponseReserveDomain> call(ResponseReserveDomain responseReserveDomain) {
                        if (responseReserveDomain == null) {
                            throw new RuntimeException();
                        } else {
                            return Observable.just(responseReserveDomain);
                        }
                    }
                });
    }

}
