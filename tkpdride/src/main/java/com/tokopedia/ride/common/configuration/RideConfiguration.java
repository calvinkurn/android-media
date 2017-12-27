package com.tokopedia.ride.common.configuration;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;

/**
 * Created by alvarisi on 3/27/17.
 */

public class RideConfiguration {
    private static final String RIDE_CONFIGURATION = "RIDE_CONFIGURATION";
    private static final String RIDE_SOURCE = "RIDE_SOURCE";
    private static final String RIDE_DESTINATION = "RIDE_DESTINATION";
    private static final String KEY_REQUEST_ID = "request_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_CANCELLATION_FEE = "cancellation_fee";
    private static final String KEY_DEVICE_IN_VEHICLE = "in_vehicle";
    private static final String DEFAULT_EMPTY_VALUE = "";

    private static final String KEY_USER_STATE = "user_state";

    private static final int STATE_WAITING_FOR_DRIVER = 1;


    private Context context;

    public RideConfiguration(Context context) {
        this.context = context;
    }

    public void setActiveRequestId(String requestId) {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        cache.putString(KEY_REQUEST_ID, requestId);
        cache.applyEditor();
    }

    public String getActiveRequestId() {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        return cache.getString(KEY_REQUEST_ID, DEFAULT_EMPTY_VALUE);
    }

    public void clearActiveRequestId() {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        cache.putString(KEY_REQUEST_ID, DEFAULT_EMPTY_VALUE);
        cache.applyEditor();
    }

    public void saveActiveProductName(String productName) {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        cache.putString(KEY_PRODUCT_NAME, productName);
        cache.applyEditor();
    }

    public void clearActiveProductName() {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        cache.putString(KEY_PRODUCT_NAME, DEFAULT_EMPTY_VALUE);
        cache.applyEditor();
    }

    public String getActiveProductName() {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        return cache.getString(KEY_PRODUCT_NAME, DEFAULT_EMPTY_VALUE);
    }

    public void clearActiveRequestCancellationFee() {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        cache.putString(KEY_CANCELLATION_FEE, DEFAULT_EMPTY_VALUE);
        cache.applyEditor();
    }

    public void saveActiveRequestCancellationFee(String cancellationFee) {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        cache.putString(KEY_CANCELLATION_FEE, cancellationFee);
        cache.applyEditor();
    }

    public String getActiveCancellationFee() {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        return cache.getString(KEY_CANCELLATION_FEE, DEFAULT_EMPTY_VALUE);
    }

    public void saveDeviceInVehicle(boolean inVehicle) {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        cache.putBoolean(KEY_DEVICE_IN_VEHICLE, inVehicle);
        cache.applyEditor();
    }

    public boolean isDeviceInVehicle() {
        LocalCacheHandler cache = new LocalCacheHandler(context, RIDE_CONFIGURATION);
        return cache.getBoolean(KEY_DEVICE_IN_VEHICLE, false);
    }
}
