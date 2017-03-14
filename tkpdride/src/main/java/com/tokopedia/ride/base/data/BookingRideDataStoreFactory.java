package com.tokopedia.ride.base.data;

import com.tokopedia.ride.base.data.source.api.UberApi;
import com.tokopedia.ride.base.data.source.cloud.CloudBookingRideDataStore;

/**
 * Created by alvarisi on 3/14/17.
 */

public class BookingRideDataStoreFactory {
    private final UberApi mUberApi;
    public BookingRideDataStoreFactory(UberApi uberApi) {
        mUberApi = uberApi;
    }

    public BookingRideDataStore createCloudDataStore(){
        return new CloudBookingRideDataStore(mUberApi);
    }
}
