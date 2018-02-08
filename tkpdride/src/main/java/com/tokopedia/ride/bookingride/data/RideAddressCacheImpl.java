package com.tokopedia.ride.bookingride.data;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.ride.common.ride.data.entity.RideAddressEntity;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 5/31/17.
 */

public class RideAddressCacheImpl implements RideAddressCache {

    private GlobalCacheManager cacheManager;
    private static final String RIDE_ADDRESS_CACHE = "RIDE_ADDRESS_CACHE";

    public RideAddressCacheImpl() {
        cacheManager = new GlobalCacheManager();
    }

    @Override
    public void put(List<RideAddressEntity> entities) {
        cacheManager.delete(RIDE_ADDRESS_CACHE);

        Type type = new TypeToken<List<RideAddressEntity>>() {
        }.getType();
        // set value
        cacheManager.setKey(RIDE_ADDRESS_CACHE);
        cacheManager.setValue(CacheUtil.convertListModelToString(entities, type));
        cacheManager.setCacheDuration(259200);
        cacheManager.store();
    }

    @Override
    public boolean isCached() {
        try {
            String cache = cacheManager.getValueString(RIDE_ADDRESS_CACHE);
            return !TextUtils.isEmpty(cache);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public void evictAll() {
        cacheManager.delete(RIDE_ADDRESS_CACHE);
    }

    @Override
    public Observable<List<RideAddressEntity>> getCache() {
        Type type = new TypeToken<List<RideAddressEntity>>() {
        }.getType();
        try {
            String cache = cacheManager.getValueString(RIDE_ADDRESS_CACHE);
            List<RideAddressEntity> entities = CacheUtil.convertStringToListModel(cache, type);
            return Observable.just(entities);
        } catch (RuntimeException e) {
            return Observable.empty();
        }

    }
}
