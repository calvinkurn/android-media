package com.tokopedia.ride.bookingride.data;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.ride.bookingride.data.entity.PeopleAddressEntity;
import com.tokopedia.ride.bookingride.data.entity.PeopleAddressResponse;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * Created by alvarisi on 4/7/17.
 */

public class PeopleAddressCacheImpl implements PeopleAddressCache {
    private GlobalCacheManager cacheManager;
    private static final String PEOPLE_ADDRESS_CACHE = "PEOPLE_ADDRESS_CACHE";
    public PeopleAddressCacheImpl() {
        cacheManager = new GlobalCacheManager();
    }

    @Override
    public void put(PeopleAddressResponse entity) {
        cacheManager.delete(PEOPLE_ADDRESS_CACHE);

        Type type = new TypeToken<PeopleAddressResponse>() {
        }.getType();
        // set value
        cacheManager.setKey(PEOPLE_ADDRESS_CACHE);
        cacheManager.setValue(CacheUtil.convertModelToString(entity, type));
        cacheManager.setCacheDuration(3600);
        cacheManager.store();
    }

    @Override
    public boolean isCached() {
        PeopleAddressResponse entity = null;
        try {
            entity = cacheManager.getConvertObjData(PEOPLE_ADDRESS_CACHE, PeopleAddressResponse.class);
        } catch (RuntimeException e) {
            return false;
        }
        return entity != null;
    }

    @Override
    public void evictAll() {
        cacheManager.delete(PEOPLE_ADDRESS_CACHE);
    }

    @Override
    public Observable<PeopleAddressResponse> getCache() {
        PeopleAddressResponse entity = null;
        try {
            entity = cacheManager.getConvertObjData(PEOPLE_ADDRESS_CACHE, PeopleAddressResponse.class);
        } catch (RuntimeException e) {
            return Observable.empty();
        }
        return Observable.just(entity);
    }
}
