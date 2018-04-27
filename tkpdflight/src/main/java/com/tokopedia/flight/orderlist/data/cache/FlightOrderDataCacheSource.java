package com.tokopedia.flight.orderlist.data.cache;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 27/04/18.
 */

public class FlightOrderDataCacheSource {

    private static long FLIGHT_DETAIL_CACHE_TIMEOUT = TimeUnit.MINUTES.toSeconds(10);
    private static String FLIGHT_DETAIL_CACHE_KEY = "FLIGHT_ORDER_CACHE";

    private CacheManager cacheManager;

    @Inject
    public FlightOrderDataCacheSource(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Observable<Boolean> isExpired() {
        return Observable.just(cacheManager.isExpired(FLIGHT_DETAIL_CACHE_KEY));
    }

    public Observable<Boolean> deleteCache() {
        cacheManager.delete(FLIGHT_DETAIL_CACHE_KEY);
        return Observable.just(true);
    }

    public void saveCache(OrderEntity orderEntity) {
        cacheManager.delete(FLIGHT_DETAIL_CACHE_KEY);

        Type type = new TypeToken<OrderEntity>(){}.getType();

        cacheManager.save(FLIGHT_DETAIL_CACHE_KEY,
                CacheUtil.convertModelToString(orderEntity, type),
                FLIGHT_DETAIL_CACHE_TIMEOUT);
    }
}
