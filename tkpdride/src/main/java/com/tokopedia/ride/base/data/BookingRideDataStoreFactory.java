package com.tokopedia.ride.base.data;

import com.tokopedia.ride.base.data.source.api.RideApi;
import com.tokopedia.ride.base.data.source.api.UberApi;
import com.tokopedia.ride.base.data.source.cloud.CloudBookingRideDataStore;

/**
 * Created by alvarisi on 3/14/17.
 */

public class BookingRideDataStoreFactory {
    private final RideApi mRideApi;
    public BookingRideDataStoreFactory(RideApi uberApi) {
        mRideApi = uberApi;
    }

    public BookingRideDataStore createCloudDataStore(){
        return new CloudBookingRideDataStore(mRideApi);
    }
}
