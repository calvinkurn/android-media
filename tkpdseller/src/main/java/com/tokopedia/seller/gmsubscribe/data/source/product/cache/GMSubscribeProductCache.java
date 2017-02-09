package com.tokopedia.seller.gmsubscribe.data.source.product.cache;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GMSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GMServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModelGroup;

import rx.Observable;

import static android.text.TextUtils.isEmpty;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GMSubscribeProductCache {
    private static final int ONE_HOUR_IN_MILISECONDS = 60 * 60;
    private static final String GM_SUBSCRIBE_PRODUCT = "GM_SUBSCRIBE_PRODUCT";
    private final GlobalCacheManager globalCacheManager;

    public GMSubscribeProductCache(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<String> getProduct() {
        return Observable.just(globalCacheManager.getValueString(GM_SUBSCRIBE_PRODUCT));
    }

    public void storeProduct(String s) {
        globalCacheManager.setKey(GM_SUBSCRIBE_PRODUCT);
        globalCacheManager.setValue(s);
        globalCacheManager.setCacheDuration(ONE_HOUR_IN_MILISECONDS);
        globalCacheManager.store();

    }

    public Observable<Boolean> clearCache() {
        globalCacheManager.delete(GM_SUBSCRIBE_PRODUCT);
        return Observable.just(true);
    }
}
