package com.tokopedia.ride.common.configuration;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.tkpd.library.utils.LocalCacheHandler;

/**
 * Created by alvarisi on 3/27/17.
 */

public class MapConfiguration {
    private static final String MAP_CONFIGURATION = "MAP_CONFIGURATION";
    private static final String MAP_DEFAULT_LATITUDE = "MAP_DEFAULT_LATITUDE";
    private static final String MAP_DEFAULT_LONGITUDE = "MAP_DEFAULT_LONGITUDE";

    public static final LatLng DEFAULT_LATLNG = new LatLng(-6.175794, 106.826457);

    private Context context;

    public MapConfiguration(Context context) {
        this.context = context;
    }

    public void setDefaultLocation(double latitude, double longitude) {
        LocalCacheHandler cache = new LocalCacheHandler(context, MAP_CONFIGURATION);
        cache.putLong(MAP_DEFAULT_LATITUDE, Double.doubleToRawLongBits(latitude));
        cache.putLong(MAP_DEFAULT_LONGITUDE, Double.doubleToRawLongBits(longitude));
        cache.applyEditor();
    }

    public double getDefaultLatitude() {
        LocalCacheHandler cache = new LocalCacheHandler(context, MAP_CONFIGURATION);
        return Double.longBitsToDouble(cache.getLong(MAP_DEFAULT_LATITUDE, Double.doubleToLongBits(DEFAULT_LATLNG.latitude)));
    }

    public double getDefaultLongitude() {
        LocalCacheHandler cache = new LocalCacheHandler(context, MAP_CONFIGURATION);
        return Double.longBitsToDouble(cache.getLong(MAP_DEFAULT_LONGITUDE, Double.doubleToLongBits(DEFAULT_LATLNG.longitude)));
    }
}
