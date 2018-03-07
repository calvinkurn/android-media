package com.tokopedia.tkpdtrain.station.data;

import com.tokopedia.tkpdtrain.common.constant.TrainApi;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.tkpdtrain.station.data.specification.Specification;
import com.tokopedia.tkpdtrain.station.domain.model.FlightStation;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationDataStoreFactory {
    private TrainApi trainApi;
    private TrainStationDbDataStore trainStationDbDataStore;
    private TrainStationCloudDataStore trainStationCloudDataStore;
    private TrainStationCacheDataStore trainStationCacheDataStore;

    public TrainStationDataStoreFactory(TrainApi trainApi) {
        this.trainApi = trainApi;
    }

    public Observable<List<FlightStation>> getStations(final Specification specification) {
        return trainStationCacheDataStore.isExpired().flatMap(new Func1<Boolean, Observable<List<FlightStation>>>() {
            @Override
            public Observable<List<FlightStation>> call(Boolean isExpired) {
                if (isExpired) {
                    return trainStationDbDataStore.deleteAll().flatMap(new Func1<Boolean, Observable<List<FlightStation>>>() {
                        @Override
                        public Observable<List<FlightStation>> call(Boolean aBoolean) {
                            if (aBoolean)
                                return getDatasFromCloud(specification);
                            else
                                return Observable.empty();
                        }
                    });
                } else {
                    return trainStationDbDataStore.getDatas(specification).flatMap(new Func1<List<FlightStation>,Observable< List<FlightStation>>>() {
                        @Override
                        public Observable< List<FlightStation>> call(List<FlightStation> flightStations) {
                            if (flightStations != null && flightStations.size() > 0)
                                return Observable.just(flightStations);
                            else
                                return getDatasFromCloud(specification);
                        }
                    });
                }
            }
        });
    }

    private Observable<List<FlightStation>> getDatasFromCloud(final Specification specification) {
        return trainStationCloudDataStore.getDatas(specification)
                .doOnNext(new Action1<List<TrainStationIslandEntity>>() {
                    @Override
                    public void call(List<TrainStationIslandEntity> trainStationIslandEntities) {
                        trainStationDbDataStore.insertAll(trainStationIslandEntities);
                    }
                })
                .flatMap(new Func1<List<TrainStationIslandEntity>, Observable<List<FlightStation>>>() {
                    @Override
                    public Observable<List<FlightStation>> call(List<TrainStationIslandEntity> trainStationIslandEntities) {
                        return trainStationDbDataStore
                                .insertAll(trainStationIslandEntities)
                                .flatMap(new Func1<Boolean, Observable<List<FlightStation>>>() {
                                    @Override
                                    public Observable<List<FlightStation>> call(Boolean isSuccessInsertData) {
                                        if (!isSuccessInsertData) {
                                            return Observable.empty();
                                        } else {
                                            return trainStationCacheDataStore.updateExpiredTime()
                                                    .flatMap(new Func1<Boolean, Observable<List<FlightStation>>>() {
                                                        @Override
                                                        public Observable<List<FlightStation>> call(Boolean isSuccessUpdateExpiredTime) {
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

    public Observable<FlightStation> getStation(final Specification specification) {
        return trainStationCacheDataStore.isExpired().flatMap(new Func1<Boolean, Observable<FlightStation>>() {
            @Override
            public Observable<FlightStation> call(Boolean isExpired) {
                if (isExpired) {
                    return trainStationDbDataStore.deleteAll().flatMap(new Func1<Boolean, Observable<FlightStation>>() {
                        @Override
                        public Observable<FlightStation> call(Boolean aBoolean) {
                            if (aBoolean)
                                return getDataFromCloud(specification);
                            else
                                return Observable.empty();
                        }
                    });
                } else {
                    return trainStationDbDataStore.getData(specification).flatMap(new Func1<FlightStation, Observable<FlightStation>>() {
                        @Override
                        public Observable<FlightStation> call(FlightStation flightStation) {
                            if (flightStation != null){
                                return Observable.just(flightStation);
                            }
                            return getDataFromCloud(specification);
                        }
                    });
                }
            }
        });
    }

    private Observable<FlightStation> getDataFromCloud(final Specification specification) {
        return trainStationCloudDataStore.getData(specification)
                .doOnNext(new Action1<TrainStationIslandEntity>() {
                    @Override
                    public void call(TrainStationIslandEntity trainStationIslandEntities) {
                        trainStationDbDataStore.insert(trainStationIslandEntities);
                    }
                })
                .flatMap(new Func1<TrainStationIslandEntity, Observable<FlightStation>>() {
                    @Override
                    public Observable<FlightStation> call(TrainStationIslandEntity trainStationIslandEntities) {
                        return trainStationDbDataStore
                                .insert(trainStationIslandEntities)
                                .flatMap(new Func1<Boolean, Observable<FlightStation>>() {
                                    @Override
                                    public Observable<FlightStation> call(Boolean isSuccessInsertData) {
                                        if (!isSuccessInsertData) {
                                            return Observable.empty();
                                        } else {
                                            return trainStationCacheDataStore.updateExpiredTime()
                                                    .flatMap(new Func1<Boolean, Observable<FlightStation>>() {
                                                                 @Override
                                                                 public Observable<FlightStation> call(Boolean isSuccessUpdateExpiredTime) {
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
