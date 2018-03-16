package com.tokopedia.tkpdtrain.search.data;

import com.tokopedia.tkpdtrain.common.specification.AndDbFlowSpecification;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.common.specification.Specification;
import com.tokopedia.tkpdtrain.search.data.entity.ScheduleAvailabilityEntity;
import com.tokopedia.tkpdtrain.search.data.entity.TrainListSchedulesEntity;
import com.tokopedia.tkpdtrain.search.domain.FilterParam;
import com.tokopedia.tkpdtrain.search.domain.mapper.AvailabilityKeysMapper;
import com.tokopedia.tkpdtrain.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;

import java.util.List;

import rx.Observable;
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
        return cloudDataStore.getDatasSchedule(specification)
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

    public Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(String idTrain) {
        return cloudDataStore.getDatasAvailability(idTrain)
                .flatMap(new Func1<List<ScheduleAvailabilityEntity>, Observable<List<TrainScheduleViewModel>>>() {
                    @Override
                    public Observable<List<TrainScheduleViewModel>> call(final List<ScheduleAvailabilityEntity> scheduleAvailabilityEntities) {
                        return dbDataStore.updateDataAvailability(scheduleAvailabilityEntities)
                                .flatMap(new Func1<Boolean, Observable<List<TrainScheduleViewModel>>>() {
                                    @Override
                                    public Observable<List<TrainScheduleViewModel>> call(Boolean isSuccessSavedData) {
                                        if (!isSuccessSavedData) {
                                            return Observable.empty();
                                        } else {
                                            return dbDataStore.getDatas(new TrainAvailabilitySpecification(scheduleAvailabilityEntities));
                                        }
                                    }
                                });
                    }
                });
    }

    public Observable<List<TrainSchedule>> getFilteredAndSortedSchedule(FilterParam filterParam, int sortOptionId) {
        DbFlowSpecification specification = new TrainSchedulePriceFilterSpecification(filterParam.getMinPrice(), filterParam.getMaxPrice());
        if (!filterParam.getTrainClass().isEmpty()) {
            specification = new AndDbFlowSpecification(specification,
                    new TrainScheduleClassFilterSpecification(filterParam.getTrainClass()));
        }
        if (!filterParam.getTrains().isEmpty()) {
            specification = new AndDbFlowSpecification(specification,
                    new TrainScheduleNameFilterSpecification(filterParam.getTrains()));
        }
        specification = new AndDbFlowSpecification(specification,
                new TrainScheduleSortSpecification(sortOptionId));
        return dbDataStore.getDatas(specification);
    }

}
