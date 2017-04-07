package com.tokopedia.ride.bookingride.data;

import com.tokopedia.ride.bookingride.data.entity.PeopleAddressResponse;

import java.util.Map;

import rx.Observable;

/**
 * Created by alvarisi on 4/6/17.
 */

public interface PeopleAddressDataStore {
    Observable<PeopleAddressResponse> getPeopleAddress(Map<String, Object> param);
}
