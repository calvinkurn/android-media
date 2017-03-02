package com.tokopedia.seller.gmsubscribe.data.source.product.cache;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GmServiceModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductCache {
    private static final int ONE_HOUR_IN_MILISECONDS = 60 * 60;
    private static final String GM_SUBSCRIBE_PRODUCT = "GM_SUBSCRIBE_PRODUCT";
    private final GlobalCacheManager globalCacheManager;

    public GmSubscribeProductCache(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<GmServiceModel> getProduct() {
        return Observable.just(true)
                .map(new Func1<Boolean, GmServiceModel>() {
                         @Override
                         public GmServiceModel call(Boolean aBoolean) {
                             return globalCacheManager.getConvertObjData(
                                     GM_SUBSCRIBE_PRODUCT,
                                     GmServiceModel.class
                             );
                         }
                     }
                )
                .map(new CheckNull());
    }

    public void storeProduct(GmServiceModel model) {
        globalCacheManager.setKey(GM_SUBSCRIBE_PRODUCT);
        globalCacheManager.setValue(
                CacheUtil.convertModelToString(
                        model,
                        new TypeToken<GmServiceModel>() {
                        }.getType()
                )
        );
        globalCacheManager.setCacheDuration(ONE_HOUR_IN_MILISECONDS);
        globalCacheManager.store();

    }

    public Observable<Boolean> clearCache() {
        globalCacheManager.delete(GM_SUBSCRIBE_PRODUCT);
        return Observable.just(true);
    }

    private class CheckNull implements Func1<GmServiceModel, GmServiceModel> {
        @Override
        public GmServiceModel call(GmServiceModel gmServiceModel) {
            if (gmServiceModel == null) {
                throw new RuntimeException("Cache is null!!");
            }
            return gmServiceModel;
        }
    }
}
