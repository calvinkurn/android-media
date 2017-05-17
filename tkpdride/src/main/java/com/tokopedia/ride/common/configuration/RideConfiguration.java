package com.tokopedia.ride.common.configuration;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.data.RideRequestEntityMapper;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

import java.lang.reflect.Type;

/**
 * Created by alvarisi on 3/27/17.
 */

public class RideConfiguration {
    private static final String RIDE_CONFIGURATION = "RIDE_CONFIGURATION";
    private static final String RIDE_SOURCE = "RIDE_SOURCE";
    private static final String RIDE_DESTINATION = "RIDE_DESTINATION";
    private static final String KEY_REQUEST_ID = "request_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String DEFAULT_EMPTY_VALUE = "";

    private static final String KEY_USER_STATE = "user_state";

    private static final int STATE_WAITING_FOR_DRIVER = 1;

    private GlobalCacheManager cacheManager;

    private Context context;

    public RideConfiguration(Context context) {
        cacheManager = new GlobalCacheManager();
        this.context = context;
    }

    public void clearActiveRequest() {
        cacheManager.delete(RIDE_CONFIGURATION);
        cacheManager.delete(RIDE_SOURCE);
        cacheManager.delete(RIDE_DESTINATION);
    }

    public boolean isWaitingDriverState() {
        RideRequestEntity entity = null;
        PlacePassViewModel source, destination;
        try {
            destination = cacheManager.getConvertObjData(RIDE_DESTINATION, PlacePassViewModel.class);
            source = cacheManager.getConvertObjData(RIDE_SOURCE, PlacePassViewModel.class);
            entity = cacheManager.getConvertObjData(RIDE_CONFIGURATION, RideRequestEntity.class);
        } catch (RuntimeException e) {
            return false;
        }
        return entity != null && source != null && destination != null;
    }

    public void setActiveRequest(RideRequest request) {
        // initialize class you want to be converted from string
        Type type = new TypeToken<RideRequest>() {
        }.getType();
        // set value
        cacheManager.setKey(RIDE_CONFIGURATION);
        cacheManager.setValue(CacheUtil.convertModelToString(request, type));
        cacheManager.setCacheDuration(5000);
        cacheManager.store();
    }

    public String getActiveRequest() {
        RideRequestEntity entity = cacheManager.getConvertObjData(RIDE_CONFIGURATION, RideRequestEntity.class);
        RideRequestEntityMapper mapper = new RideRequestEntityMapper();
        RideRequest request = mapper.transform(entity);
        return request.getRequestId();
    }

    public RideRequest getActiveRequestObj() {
        RideRequestEntity entity = cacheManager.getConvertObjData(RIDE_CONFIGURATION, RideRequestEntity.class);
        RideRequestEntityMapper mapper = new RideRequestEntityMapper();
        return mapper.transform(entity);
    }

    public void setActiveSource(PlacePassViewModel activeSource) {
        Type type = new TypeToken<PlacePassViewModel>() {
        }.getType();
        // set value
        cacheManager.setKey(RIDE_SOURCE);
        cacheManager.setValue(CacheUtil.convertModelToString(activeSource, type));
        cacheManager.setCacheDuration(5000);
        cacheManager.store();
    }

    public void setActiveDestination(PlacePassViewModel activeDestination) {
        Type type = new TypeToken<PlacePassViewModel>() {
        }.getType();
        // set value
        cacheManager.setKey(RIDE_DESTINATION);
        cacheManager.setValue(CacheUtil.convertModelToString(activeDestination, type));
        cacheManager.setCacheDuration(5000);
        cacheManager.store();
    }

    public PlacePassViewModel getActiveSource() {
        PlacePassViewModel place = null;
        try {
            place = cacheManager.getConvertObjData(RIDE_SOURCE, PlacePassViewModel.class);
        } catch (RuntimeException e) {
            return null;
        }
        return place;
    }

    public PlacePassViewModel getActiveDestination() {
        PlacePassViewModel place = null;
        try {
            place = cacheManager.getConvertObjData(RIDE_DESTINATION, PlacePassViewModel.class);
        } catch (RuntimeException e) {
            return null;
        }
        return place;
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
}
