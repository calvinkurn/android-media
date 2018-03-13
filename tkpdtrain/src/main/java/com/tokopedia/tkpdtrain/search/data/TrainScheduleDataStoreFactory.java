package com.tokopedia.tkpdtrain.search.data;

import com.tokopedia.tkpdtrain.common.specification.Specification;
import com.tokopedia.tkpdtrain.search.data.entity.TrainListSchedulesEntity;
import com.tokopedia.tkpdtrain.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.tkpdtrain.search.domain.mapper.AvailabilityKeysMapper;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleDataStoreFactory {

    private TrainScheduleDbDataStore dbDataStore;
    private TrainScheduleCacheDataStore cacheDataStore;
    private TrainScheduleCloudDataStore cloudDataStore;

    public TrainScheduleDataStoreFactory(TrainScheduleDbDataStore dbDataStore,
                                         TrainScheduleCacheDataStore cacheDataStore,
                                         TrainScheduleCloudDataStore cloudDataStore) {
        this.dbDataStore = dbDataStore;
        this.cacheDataStore = cacheDataStore;
        this.cloudDataStore = cloudDataStore;
    }

    public Observable<List<AvailabilityKeySchedule>> getScheduleTrain(final Specification specification) {
        return dbDataStore.deleteAll().flatMap(new Func1<Boolean, Observable<List<AvailabilityKeySchedule>>>() {
            @Override
            public Observable<List<AvailabilityKeySchedule>> call(Boolean isSuccessDelete) {
                if (isSuccessDelete) {
                    return getDataFromCloud(specification);
                } else {
                    return Observable.empty();
                }
            }
        });
    }

    private Observable<List<AvailabilityKeySchedule>> getDataFromCloud(Specification specification) {
        return cloudDataStore.getDatas(specification)
                .doOnNext(new Action1<TrainListSchedulesEntity>() {
                    @Override
                    public void call(TrainListSchedulesEntity trainListSchedulesEntity) {
                        dbDataStore.insertAll(trainListSchedulesEntity.getTrainSchedules());
                    }
                })
                .flatMap(new Func1<TrainListSchedulesEntity, Observable<List<AvailabilityKeySchedule>>>() {
                    @Override
                    public Observable<List<AvailabilityKeySchedule>> call(final TrainListSchedulesEntity trainListSchedulesEntity) {
                        return dbDataStore.insertAll(trainListSchedulesEntity.getTrainSchedules())
                                .flatMap(new Func1<Boolean, Observable<List<AvailabilityKeySchedule>>>() {
                                    @Override
                                    public Observable<List<AvailabilityKeySchedule>> call(Boolean isSuccessSaveData) {
                                        if (!isSuccessSaveData) {
                                            return Observable.empty();
                                        } else {
                                            return Observable.just(trainListSchedulesEntity.getAvailabilityKeys())
                                                    .map(new AvailabilityKeysMapper());
                                        }
                                    }
                                });
                    }
                });
    }
}
