package com.tokopedia.seller.gmsubscribe.data.source.product;

import com.google.gson.Gson;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GMSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GMServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModelGroup;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductDataSource {


    private final GmSubscribeProductCache gmSubscribeProductCache;
    private final GMSubscribeProductCloud gmSubscribeProductCloud;
    private final GmSubscribeProductMapper gmSubscribeProductMapper;
    private final Gson gson;


    public GmSubscribeProductDataSource(GmSubscribeProductCache gmSubscribeProductCache,
                                        GMSubscribeProductCloud gmSubscribeProductCloud,
                                        GmSubscribeProductMapper gmSubscribeProductMapper,
                                        Gson gson) {
        this.gmSubscribeProductCache = gmSubscribeProductCache;
        this.gmSubscribeProductCloud = gmSubscribeProductCloud;
        this.gmSubscribeProductMapper = gmSubscribeProductMapper;
        this.gson = gson;
    }

    public Observable<GmProductDomainModelGroup> getData() {
        return gmSubscribeProductCache
                .getProduct()
                .flatMap(new CheckCache())
                .onErrorResumeNext(new GetDataFromCloud())
                .flatMap(new ConvertToObject());
    }

    public Observable<Boolean> clearData() {
        return gmSubscribeProductCache.clearCache();
    }

    private class GetDataFromCloud implements Func1<Throwable, Observable<String>> {
        @Override
        public Observable<String> call(Throwable throwable) {
            return gmSubscribeProductCloud
                    .getProduct()
                    .doOnNext(new SaveToCache());
        }
    }

    private class SaveToCache implements Action1<String> {
        @Override
        public void call(String s) {
            gmSubscribeProductCache.storeProduct(s);
        }
    }

    private class ConvertToObject implements Func1<String, Observable<GmProductDomainModelGroup>> {
        @Override
        public Observable<GmProductDomainModelGroup> call(String string) {
            return Observable.just(gson.fromJson(string, GMServiceModel.class)).map(gmSubscribeProductMapper);
        }
    }

    private class CheckCache implements Func1<String, Observable<String>> {
        @Override
        public Observable<String> call(String s) {
            if (s == null) {
                throw new RuntimeException();
            } else {
                return Observable.just(s);
            }
        }
    }
}
