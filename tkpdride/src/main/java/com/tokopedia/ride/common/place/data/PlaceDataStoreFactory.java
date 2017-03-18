package com.tokopedia.ride.common.place.data;

import com.tokopedia.ride.common.place.data.source.CloudPlaceDataStore;
import com.tokopedia.ride.common.place.data.source.api.PlaceApi;

/**
 * Created by alvarisi on 3/18/17.
 */

public class PlaceDataStoreFactory {
    private final PlaceApi placeApi;

    public PlaceDataStoreFactory(PlaceApi placeApi) {
        this.placeApi = placeApi;
    }

    public PlaceDataStore createCloudPlaceDataStore(){
        return new CloudPlaceDataStore(this.placeApi);
    }
}
