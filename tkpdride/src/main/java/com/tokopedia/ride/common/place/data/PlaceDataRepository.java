package com.tokopedia.ride.common.place.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.place.data.entity.DirectionEntity;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 3/18/17.
 */

public class PlaceDataRepository implements PlaceRepository {
    private final PlaceDataStoreFactory placeDataStoreFactory;
    private final DirectionEntityMapper directionEntityMapper;

    public PlaceDataRepository(PlaceDataStoreFactory placeDataStoreFactory, DirectionEntityMapper directionEntityMapper) {
        this.placeDataStoreFactory = placeDataStoreFactory;
        this.directionEntityMapper = directionEntityMapper;
    }

    @Override
    public Observable<OverviewPolyline> getOveriewPolyline(TKPDMapParam<String, Object> param) {
        return placeDataStoreFactory.createCloudPlaceDataStore()
                .getDirection("json", param)
                .map(new Func1<DirectionEntity, OverviewPolyline>() {
                    @Override
                    public OverviewPolyline call(DirectionEntity directionEntity) {
                        return directionEntityMapper.transformToOverviewPolyline(directionEntity);
                    }
                });
    }
}
