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
    private static final String KEY_USER_STATE = "user_state";

    private static final int STATE_WAITING_FOR_DRIVER = 1;

    GlobalCacheManager cacheManager;

    public RideConfiguration() {
        cacheManager = new GlobalCacheManager();
    }

    public void clearActiveRequest() {
        cacheManager.delete(RIDE_CONFIGURATION);
        cacheManager.delete(RIDE_SOURCE);
        cacheManager.delete(RIDE_DESTINATION);
    }

    public boolean isWaitingDriverState() {
        RideRequestEntity entity = null;
        try {
            entity = cacheManager.getConvertObjData(RIDE_CONFIGURATION, RideRequestEntity.class);
        } catch (RuntimeException e) {
            return false;
        }
        return entity != null;
    }

    public void setActiveRequest(RideRequestEntity request) {
        // initialize class you want to be converted from string
        Type type = new TypeToken<RideRequestEntity>() {
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

    public RideRequest getActiveRequestObj(){
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
}
