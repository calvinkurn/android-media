package com.tokopedia.ride.common.place.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.place.data.entity.DirectionEntity;
import com.tokopedia.ride.common.place.data.entity.DistanceMatrixEntity;
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
}
