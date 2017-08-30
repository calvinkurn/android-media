package com.tokopedia.ride.common.animator;

import android.animation.TypeEvaluator;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alvarisi on 4/17/17.
 */

public class RouteEvaluator implements TypeEvaluator<LatLng> {
    @Override
    public LatLng evaluate(float t, LatLng startPoint, LatLng endPoint) {
        if (startPoint == null || endPoint == null) {
            return null;
        }

        double lat = startPoint.latitude + t * (endPoint.latitude - startPoint.latitude);
        double lng = startPoint.longitude + t * (endPoint.longitude - startPoint.longitude);
        return new LatLng(lat, lng);
    }
}
