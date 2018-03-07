package com.tokopedia.tkpdtrain.station.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.abstraction.base.data.source.database.DataDBSource;
import com.tokopedia.tkpdtrain.station.data.databasetable.TrainStationDb;
import com.tokopedia.tkpdtrain.station.data.entity.TrainCityEntity;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationEntity;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.tkpdtrain.station.data.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.station.data.specification.DbFlowWithOrderSpecification;
import com.tokopedia.tkpdtrain.station.data.specification.Specification;
import com.tokopedia.tkpdtrain.station.domain.model.FlightStation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationDbDataStore implements TrainDataDBSource<TrainStationIslandEntity, FlightStation> {

    @Override
    public Observable<Boolean> isDataAvailable() {
        return null;
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return null;
    }

    @Override
    public Observable<Boolean> insert(final TrainStationIslandEntity data) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                for (TrainCityEntity trainCityEntity : data.getCities()) {
                    for (TrainStationEntity trainStationEntity : trainCityEntity.getStations()) {
                        TrainStationDb trainStationDb = new TrainStationDb();
                        trainStationDb.setIslandId(data.getId());
                        trainStationDb.setIslandName(data.getName());
                        trainStationDb.setCityId(trainCityEntity.getId());
                        trainStationDb.setCityName(trainCityEntity.getName());
                        trainStationDb.setPopularityOrder(trainStationEntity.getPopularityOrder());
                        trainStationDb.setStationId(trainStationEntity.getId());
                        trainStationDb.setStationName(trainStationEntity.getName());
                        trainStationDb.setStationCode(trainStationEntity.getCode());
                        trainStationDb.setStationDisplayName(trainStationEntity.getDisplayName());
                        trainStationDb.setStationStatus(trainStationEntity.getStatus());
                        trainStationDb.save();
                    }
                }
            }
        });
    }

    @Override
    public Observable<Boolean> insertAll(List<TrainStationIslandEntity> datas) {
        return Observable.just(datas)
                .flatMapIterable(new Func1<List<TrainStationIslandEntity>, Iterable<TrainStationIslandEntity>>() {
                    @Override
                    public Iterable<TrainStationIslandEntity> call(List<TrainStationIslandEntity> trainStationIslandEntities) {
                        return trainStationIslandEntities;
                    }
                }).flatMap(new Func1<TrainStationIslandEntity, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(TrainStationIslandEntity trainStationIslandEntity) {
                        return insert(trainStationIslandEntity);
                    }
                });
    }


    @Override
    public Observable<List<FlightStation>> getDatas(final Specification specification) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<TrainStationDb>>() {
            @Override
            public void call(Subscriber<? super List<TrainStationDb>> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).toCondition();
                }
                List<OrderBy> orderBies = new ArrayList<>();
                if (specification instanceof DbFlowWithOrderSpecification) {
                    orderBies = ((DbFlowWithOrderSpecification) specification).toOrder();
                }
                List<TrainStationDb> flightAirportDBList = new Select()
                        .from(TrainStationDb.class)
                        .where(conditions)
                        .orderByAll(orderBies)
                        .queryList();

                subscriber.onNext(flightAirportDBList);
            }
        }).map(new Func1<List<TrainStationDb>, List<FlightStation>>() {
            @Override
            public List<FlightStation> call(List<TrainStationDb> trainStationDbs) {
                return null;
            }
        });
    }

    @Override
    public Observable<Integer> getCount(Specification specification) {
        return null;
    }

    @Override
    public Observable<FlightStation> getData(final Specification specification) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<TrainStationDb>() {
            @Override
            public void call(Subscriber<? super TrainStationDb> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).toCondition();
                }
                List<OrderBy> orderBies = new ArrayList<>();
                if (specification instanceof DbFlowWithOrderSpecification) {
                    orderBies = ((DbFlowWithOrderSpecification) specification).toOrder();
                }
                TrainStationDb flightAirportDBList = new Select()
                        .from(TrainStationDb.class)
                        .where(conditions)
                        .orderByAll(orderBies)
                        .querySingle();

                subscriber.onNext(flightAirportDBList);
            }
        }).map(new Func1<TrainStationDb, FlightStation>() {
            @Override
            public FlightStation call(TrainStationDb trainStationDb) {
                return null;
            }
        });
    }
}

