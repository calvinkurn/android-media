package com.tokopedia.tkpdtrain.station.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdtrain.common.constant.TrainApi;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationEntity;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationIslandEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainCloudDataStore {
    private TrainApi trainApi;

    public TrainCloudDataStore(TrainApi trainApi) {
        this.trainApi = trainApi;
    }

    public Observable<List<TrainStationIslandEntity>> getStationsInIsland() {
        return trainApi.stationsInIsland().map(new Func1<DataResponse<List<TrainStationIslandEntity>>, List<TrainStationIslandEntity>>() {
            @Override
            public List<TrainStationIslandEntity> call(DataResponse<List<TrainStationIslandEntity>> listDataResponse) {
                return listDataResponse.getData();
            }
        });
    }

}
