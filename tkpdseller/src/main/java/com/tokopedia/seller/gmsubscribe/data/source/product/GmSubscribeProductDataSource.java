package com.tokopedia.seller.gmsubscribe.data.source.product;

import android.support.annotation.NonNull;

import com.tokopedia.seller.gmsubscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GmSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GmServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModelGroup;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductDataSource {


    private final GmSubscribeProductCache gmSubscribeProductCache;
    private final GmSubscribeProductCloud gmSubscribeProductCloud;
    private final GmSubscribeProductMapper gmSubscribeProductMapper;


    public GmSubscribeProductDataSource(GmSubscribeProductCache gmSubscribeProductCache,
                                        GmSubscribeProductCloud gmSubscribeProductCloud,
                                        GmSubscribeProductMapper gmSubscribeProductMapper) {
        this.gmSubscribeProductCache = gmSubscribeProductCache;
        this.gmSubscribeProductCloud = gmSubscribeProductCloud;
        this.gmSubscribeProductMapper = gmSubscribeProductMapper;
    }

    public Observable<GmProductDomainModelGroup> getData() {
        return gmSubscribeProductCache
                .getProduct()
                .onErrorResumeNext(getGmServiceModelObservable())
                .flatMap(new ConvertToObject());
    }

    @NonNull
    private Observable<GmServiceModel> getGmServiceModelObservable() {
        return gmSubscribeProductCloud
                .getProduct()
                .doOnNext(new SaveToCache());
    }

    public Observable<Boolean> clearData() {
        return gmSubscribeProductCache.clearCache();
    }

    private class SaveToCache implements Action1<GmServiceModel> {
        @Override
        public void call(GmServiceModel response) {
            gmSubscribeProductCache.storeProduct(response);
        }
    }

    private class ConvertToObject implements Func1<GmServiceModel, Observable<GmProductDomainModelGroup>> {
        @Override
        public Observable<GmProductDomainModelGroup> call(GmServiceModel response) {
            return Observable.just(response).map(gmSubscribeProductMapper);
        }
    }
}
