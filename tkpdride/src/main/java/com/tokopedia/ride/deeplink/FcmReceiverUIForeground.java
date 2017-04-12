package com.tokopedia.ride.deeplink;

import android.os.Bundle;

import com.tokopedia.ride.common.ride.domain.model.RideRequest;

import rx.Observable;

/**
 * Created by alvarisi on 4/12/17.
 */

public interface FcmReceiverUIForeground {
    /**
     * Called when Activity or Fragment matches with the desired target specified in the bundle notification.
     * @see FcmReceiverUIForeground
     */
    void onTargetNotification(Observable<RideRequest> oMessage);

    /**
     * Called when Activity or Fragment does not match with the desired target specified in the bundle notification.
     * @see FcmReceiverUIForeground
     */
    void onMismatchTargetNotification(Observable<RideRequest> oMessage);

    /**
     * Determines if the implementing class is interested on be notified when updating the data model or seeking for the activity/fragment to be notified.
     * @param key The value provided in the bundle notification by the server
     * @return true if the implementing class is interested on be notified
     */
    boolean matchesTarget(String key);
}
