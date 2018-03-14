package com.tokopedia.tkpdtrain.station.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdtrain.common.specification.Specification;
import com.tokopedia.tkpdtrain.common.constant.TrainApi;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.tkpdtrain.common.specification.CloudNetworkSpecification;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationCloudDataStore {
    private TrainApi trainApi;

    public TrainStationCloudDataStore(TrainApi trainApi) {
        this.trainApi = trainApi;
    }

    public Observable<List<TrainStationIslandEntity>> getDatas(Specification specification) {
        return trainApi.stationsInIsland(((CloudNetworkSpecification) specification).networkParam()).map(new Func1<DataResponse<List<TrainStationIslandEntity>>, List<TrainStationIslandEntity>>() {
            @Override
            public List<TrainStationIslandEntity> call(DataResponse<List<TrainStationIslandEntity>> listDataResponse) {
                return listDataResponse.getData();
            }
        });
    }

    public Observable<TrainStationIslandEntity> getData(Specification specification) {
        return null;
    }
}
