package com.tokopedia.ride.common.configuration;

/**
 * Created by alvarisi on 4/20/17.
 */

public interface RideStatus {
    String KEY = "RIDE_STATUS";
    String NO_DRIVER_AVAILABLE = "no_drivers_available";
    String PROCESSING = "processing";
    String ACCEPTED = "accepted";
    String ARRIVING = "arriving";
    String IN_PROGRESS = "in_progress";
    String DRIVER_CANCELED = "driver_canceled";
    String RIDER_CANCELED = "rider_canceled";
    String COMPLETED = "completed";

    interface Action{
        String KEY = "KEY_ACTION";
        String ACTION_SHARE = "ACTION_SHARE";
    }
}
