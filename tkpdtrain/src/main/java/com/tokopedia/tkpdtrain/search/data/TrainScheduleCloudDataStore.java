package com.tokopedia.tkpdtrain.search.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdtrain.common.constant.TrainApi;
import com.tokopedia.tkpdtrain.common.specification.CloudNetworkSpecification;
import com.tokopedia.tkpdtrain.common.specification.Specification;
import com.tokopedia.tkpdtrain.search.data.entity.TrainListSchedulesEntity;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleCloudDataStore {

    private TrainApi trainApi;

    public TrainScheduleCloudDataStore(TrainApi trainApi) {
        this.trainApi = trainApi;
    }

    public Observable<TrainListSchedulesEntity> getDatas(Specification specification) {
        return trainApi.schedulesTrain(((CloudNetworkSpecification) specification).networkParam())
                .map(new Func1<DataResponse<TrainListSchedulesEntity>, TrainListSchedulesEntity>() {
                    @Override
                    public TrainListSchedulesEntity call(DataResponse<TrainListSchedulesEntity> trainListSchedulesEntityDataResponse) {
                        return trainListSchedulesEntityDataResponse.getData();
                    }
                });
    }
}
