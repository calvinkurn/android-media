package com.tokopedia.tkpdtrain.station.data;

import com.tokopedia.tkpdtrain.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.tkpdtrain.common.specification.Specification;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationDataStoreFactory {
    private TrainStationDbDataStore trainStationDbDataStore;
    private TrainStationCloudDataStore trainStationCloudDataStore;
    private TrainStationCacheDataStore trainStationCacheDataStore;

    public TrainStationDataStoreFactory(TrainStationDbDataStore trainStationDbDataStore,
                                        TrainStationCloudDataStore trainStationCloudDataStore,
                                        TrainStationCacheDataStore trainStationCacheDataStore) {
        this.trainStationDbDataStore = trainStationDbDataStore;

        this.trainStationCloudDataStore = trainStationCloudDataStore;
        this.trainStationCacheDataStore = trainStationCacheDataStore;
    }

    public Observable<List<TrainStation>> getStations(final Specification specification) {
        return trainStationCacheDataStore.isExpired().flatMap(new Func1<Boolean, Observable<List<TrainStation>>>() {
            @Override
            public Observable<List<TrainStation>> call(Boolean isExpired) {
                if (isExpired) {
                    return trainStationDbDataStore.deleteAll().flatMap(new Func1<Boolean, Observable<List<TrainStation>>>() {
                        @Override
                        public Observable<List<TrainStation>> call(Boolean aBoolean) {
                            if (aBoolean)
                                return getDatasFromCloud(specification);
                            else
                                return Observable.empty();
                        }
                    });
                } else {
                    return trainStationDbDataStore.getDatas(specification).flatMap(new Func1<List<TrainStation>,Observable< List<TrainStation>>>() {
                        @Override
                        public Observable< List<TrainStation>> call(List<TrainStation> trainStations) {
                            if (trainStations != null && trainStations.size() > 0)
                                return Observable.just(trainStations);
                            else
                                return getDatasFromCloud(specification);
                        }
                    });
                }
            }
        });
    }

    private Observable<List<TrainStation>> getDatasFromCloud(final Specification specification) {
        return trainStationCloudDataStore.getDatas(specification)
                .flatMap(new Func1<List<TrainStationIslandEntity>, Observable<List<TrainStation>>>() {
                    @Override
                    public Observable<List<TrainStation>> call(List<TrainStationIslandEntity> trainStationIslandEntities) {
                        return trainStationDbDataStore
                                .insertAll(trainStationIslandEntities)
                                .flatMap(new Func1<Boolean, Observable<List<TrainStation>>>() {
                                    @Override
                                    public Observable<List<TrainStation>> call(Boolean isSuccessInsertData) {
                                        if (!isSuccessInsertData) {
                                            return Observable.empty();
                                        } else {
                                            return trainStationCacheDataStore.updateExpiredTime()
                                                    .flatMap(new Func1<Boolean, Observable<List<TrainStation>>>() {
                                                        @Override
                                                        public Observable<List<TrainStation>> call(Boolean isSuccessUpdateExpiredTime) {
                                                            if (!isSuccessUpdateExpiredTime) {
                                                                return Observable.empty();
                                                            } else {
                                                                return trainStationDbDataStore.getDatas(specification);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }

    public Observable<TrainStation> getStation(final Specification specification) {
        return trainStationCacheDataStore.isExpired().flatMap(new Func1<Boolean, Observable<TrainStation>>() {
            @Override
            public Observable<TrainStation> call(Boolean isExpired) {
                if (isExpired) {
                    return trainStationDbDataStore.deleteAll().flatMap(new Func1<Boolean, Observable<TrainStation>>() {
                        @Override
                        public Observable<TrainStation> call(Boolean aBoolean) {
                            if (aBoolean)
                                return getDataFromCloud(specification);
                            else
                                return Observable.empty();
                        }
                    });
                } else {
                    return trainStationDbDataStore.getData(specification).flatMap(new Func1<TrainStation, Observable<TrainStation>>() {
                        @Override
                        public Observable<TrainStation> call(TrainStation trainStation) {
                            if (trainStation != null){
                                return Observable.just(trainStation);
                            }
                            return getDataFromCloud(specification);
                        }
                    });
                }
            }
        });
    }

    private Observable<TrainStation> getDataFromCloud(final Specification specification) {
        return trainStationCloudDataStore.getData(specification)
                .doOnNext(new Action1<TrainStationIslandEntity>() {
                    @Override
                    public void call(TrainStationIslandEntity trainStationIslandEntities) {
                        trainStationDbDataStore.insert(trainStationIslandEntities);
                    }
                })
                .flatMap(new Func1<TrainStationIslandEntity, Observable<TrainStation>>() {
                    @Override
                    public Observable<TrainStation> call(TrainStationIslandEntity trainStationIslandEntities) {
                        return trainStationDbDataStore
                                .insert(trainStationIslandEntities)
                                .flatMap(new Func1<Boolean, Observable<TrainStation>>() {
                                    @Override
                                    public Observable<TrainStation> call(Boolean isSuccessInsertData) {
                                        if (!isSuccessInsertData) {
                                            return Observable.empty();
                                        } else {
                                            return trainStationCacheDataStore.updateExpiredTime()
                                                    .flatMap(new Func1<Boolean, Observable<TrainStation>>() {
                                                                 @Override
                                                                 public Observable<TrainStation> call(Boolean isSuccessUpdateExpiredTime) {
                                                                     if (!isSuccessUpdateExpiredTime) {
                                                                         return Observable.empty();
                                                                     } else {
                                                                         return trainStationDbDataStore.getData(specification);
                                                                     }
                                                                 }
                                                             }
                                                    );
                                        }
                                    }
                                });
                    }
                });
    }
}
