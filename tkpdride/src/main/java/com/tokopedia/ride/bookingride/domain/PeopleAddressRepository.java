package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.ride.bookingride.domain.model.PeopleAddressWrapper;

import java.util.Map;

import rx.Observable;

/**
 * Created by alvarisi on 4/6/17.
 */

public interface PeopleAddressRepository {
    Observable<PeopleAddressWrapper> getAddresses(boolean isFirst, Map<String, Object> params);
}
