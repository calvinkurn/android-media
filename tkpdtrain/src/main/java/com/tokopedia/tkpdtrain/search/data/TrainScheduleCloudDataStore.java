package com.tokopedia.tkpdtrain.search.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdtrain.common.constant.TrainApi;
import com.tokopedia.tkpdtrain.common.specification.CloudNetworkSpecification;
import com.tokopedia.tkpdtrain.common.specification.Specification;
import com.tokopedia.tkpdtrain.search.data.entity.ScheduleAvailabilityEntity;
import com.tokopedia.tkpdtrain.search.data.entity.TrainListSchedulesEntity;

import java.util.List;

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

    public Observable<TrainListSchedulesEntity> getDatasSchedule(Specification specification) {
        return trainApi.schedulesTrain(((CloudNetworkSpecification) specification).networkParam())
                .map(new Func1<DataResponse<TrainListSchedulesEntity>, TrainListSchedulesEntity>() {
                    @Override
                    public TrainListSchedulesEntity call(DataResponse<TrainListSchedulesEntity> trainListSchedulesEntityDataResponse) {
                        return trainListSchedulesEntityDataResponse.getData();
                    }
                });
    }

    public Observable<List<ScheduleAvailabilityEntity>> getDatasAvailability(String idTrain) {
        return trainApi.availabilityTrain(idTrain)
                .map(new Func1<DataResponse<List<ScheduleAvailabilityEntity>>, List<ScheduleAvailabilityEntity>>() {
                    @Override
                    public List<ScheduleAvailabilityEntity> call(DataResponse<List<ScheduleAvailabilityEntity>> listDataResponse) {
                        return listDataResponse.getData();
                    }
                });
    }
}
