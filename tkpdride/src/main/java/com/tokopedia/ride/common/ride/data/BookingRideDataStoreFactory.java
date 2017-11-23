package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.source.api.RideApi;
import com.tokopedia.ride.common.ride.data.source.cloud.CloudBookingRideDataStore;

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
    public BookingRideDataStore createDiskDataStore(){
        return new DiskBookingRideDataStore();
    }
}
