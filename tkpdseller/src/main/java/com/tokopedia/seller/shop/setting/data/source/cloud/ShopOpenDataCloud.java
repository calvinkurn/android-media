package com.tokopedia.seller.shop.setting.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.seller.shop.setting.data.source.cloud.api.TomeApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class ShopOpenDataCloud {
    private final TomeApi api;
    private final Context context;

    @Inject
    public ShopOpenDataCloud(TomeApi api, @ActivityContext Context context) {
        this.api = api;
        this.context = context;
    }

    public Observable<Boolean> checkDomainName(String domainName) {
//        return api.getDomainCheck(
//                AuthUtil.generateParamsNetwork(context));
        // TODO stub
        return Observable.just(true);
    }

    public Observable<Boolean> checkShopName(String shopName) {
//        return api.getDomainCheck(
//                AuthUtil.generateParamsNetwork(context));
        // TODO stub
        return Observable.just(true);
    }

}
