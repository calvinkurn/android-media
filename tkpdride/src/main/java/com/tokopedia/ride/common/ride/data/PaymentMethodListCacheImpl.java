package com.tokopedia.ride.common.ride.data;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodListEntity;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * Created by alvarisi on 5/31/17.
 */

public class PaymentMethodListCacheImpl implements PaymentMethodListCache {

    private GlobalCacheManager cacheManager;
    private static final String PAYMENT_METHOD_LIST_CACHE = "PAYMENT_METHOD_LIST_CACHE";
    private final int CACHE_DURATION = 86400;

    public PaymentMethodListCacheImpl() {
        cacheManager = new GlobalCacheManager();
    }

    @Override
    public void put(PaymentMethodListEntity entity) {
        cacheManager.delete(PAYMENT_METHOD_LIST_CACHE);

        Type type = new TypeToken<PaymentMethodListEntity>() {

        }.getType();
        // set value
        cacheManager.setKey(PAYMENT_METHOD_LIST_CACHE);
        cacheManager.setValue(CacheUtil.convertModelToString(entity, type));
        cacheManager.setCacheDuration(CACHE_DURATION);
        cacheManager.store();
    }

    @Override
    public boolean isCached() {
        try {
            String cache = cacheManager.getValueString(PAYMENT_METHOD_LIST_CACHE);
            return !TextUtils.isEmpty(cache);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public void evictAll() {
        cacheManager.delete(PAYMENT_METHOD_LIST_CACHE);
    }

    @Override
    public Observable<PaymentMethodListEntity> getCache() {
        Type type = new TypeToken<PaymentMethodListEntity>() {
        }.getType();
        try {
            String cache = cacheManager.getValueString(PAYMENT_METHOD_LIST_CACHE);
            PaymentMethodListEntity entities = CacheUtil.convertStringToModel(cache, type);
            return Observable.just(entities);
        } catch (RuntimeException e) {
            return Observable.empty();
        }

    }
}
