package com.tokopedia.ride.bookingride.data;

import com.tokopedia.ride.bookingride.data.entity.PeopleAddressResponse;

import java.util.Map;

import rx.Observable;

/**
 * Created by alvarisi on 4/7/17.
 */

public class DiskPeopleAddressDataStore implements PeopleAddressDataStore {
    PeopleAddressCache peopleAddressCache;
    public DiskPeopleAddressDataStore() {
        peopleAddressCache = new PeopleAddressCacheImpl();
    }

    @Override
    public Observable<PeopleAddressResponse> getPeopleAddress(Map<String, Object> param) {
        return peopleAddressCache.getCache();
    }
}
