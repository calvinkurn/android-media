package com.tokopedia.ride.bookingride.data;

import com.tokopedia.ride.bookingride.data.entity.PeopleAddressResponse;

import rx.Observable;

/**
 * Created by alvarisi on 4/7/17.
 */

public interface PeopleAddressCache {

    void put(PeopleAddressResponse entity);

    boolean isCached();

    void evictAll();

    Observable<PeopleAddressResponse> getCache();
}
