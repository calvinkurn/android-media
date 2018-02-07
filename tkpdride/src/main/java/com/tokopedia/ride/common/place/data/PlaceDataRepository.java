package com.tokopedia.ride.common.place.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.place.data.entity.DirectionEntity;
import com.tokopedia.ride.common.place.data.entity.DistanceMatrixEntity;
import com.tokopedia.ride.common.place.data.entity.ReverseGeoCodeAddress;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 3/18/17.
 */

public class PlaceDataRepository implements PlaceRepository {
    private final PlaceDataStoreFactory placeDataStoreFactory;
    private final DirectionEntityMapper directionEntityMapper;

    public PlaceDataRepository(PlaceDataStoreFactory placeDataStoreFactory) {
        this.placeDataStoreFactory = placeDataStoreFactory;
        this.directionEntityMapper = new DirectionEntityMapper();
    }

    @Override
    public Observable<List<OverviewPolyline>> getOveriewPolyline(TKPDMapParam<String, Object> param) {
        return placeDataStoreFactory.createCloudPlaceDataStore()
                .getDirection("json", param)
                .map(new Func1<DirectionEntity, List<OverviewPolyline>>() {
                    @Override
                    public List<OverviewPolyline> call(DirectionEntity directionEntity) {
                        return directionEntityMapper.transformOverViews(directionEntity);
                    }
                });
    }

    @Override
    public Observable<DistanceMatrixEntity> getDistanceMatrix(TKPDMapParam<String, Object> param) {
        return placeDataStoreFactory.createCloudPlaceDataStore()
                .getDistanceMarix("json", param);
    }

    @Override
    public Observable<ReverseGeoCodeAddress> getAddressFromGoogleAPI(final String key, final String address) {
        return placeDataStoreFactory.createCloudPlaceDataStore()
                .getAddressFromGoogleAPI(key, address).map(new Func1<JsonObject, ReverseGeoCodeAddress>() {
                    @Override
                    public ReverseGeoCodeAddress call(JsonObject jsonObject) {
                        JsonArray jsonElements = jsonObject.getAsJsonArray("results");
                        ReverseGeoCodeAddress reverseGeoCodeAddress = new ReverseGeoCodeAddress();
                        if (jsonElements != null && jsonElements.size() > 0) {
                            Gson gson = new Gson();
                            reverseGeoCodeAddress = (ReverseGeoCodeAddress) gson.fromJson(jsonElements.get(0), ReverseGeoCodeAddress.class);
                        }

                        return reverseGeoCodeAddress;
                    }
                });
    }
}
