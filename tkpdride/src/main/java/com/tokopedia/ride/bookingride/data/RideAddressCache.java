package com.tokopedia.ride.bookingride.data;

import com.tokopedia.ride.common.ride.data.entity.RideAddressEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 5/31/17.
 */

public interface RideAddressCache {

    void put(List<RideAddressEntity> entities);

    boolean isCached();

    void evictAll();

    Observable<List<RideAddressEntity>> getCache();
}
