package com.tokopedia.tkpdtrain.station.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.tkpdtrain.station.data.databasetable.TrainStationDb;
import com.tokopedia.tkpdtrain.station.data.entity.TrainCityEntity;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationEntity;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.tkpdtrain.station.data.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.station.data.specification.DbFlowWithOrderSpecification;
import com.tokopedia.tkpdtrain.station.data.specification.Specification;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;
import com.tokopedia.tkpdtrain.station.domain.model.mapper.TrainStationDbMapper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationDbDataStore implements TrainDataDBSource<TrainStationIslandEntity, TrainStation> {

    public TrainStationDbDataStore() {
    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(TrainStationDb.class).hasData());
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(TrainStationDb.class).execute();
                subscriber.onNext(true);
            }
        });
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
    public Observable<Boolean> insertAll(final List<TrainStationIslandEntity> datas) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                for (TrainStationIslandEntity entity : datas){
                    insert(entity);
                }
                subscriber.onNext(true);
            }
        });
                /*.flatMapIterable(new Func1<List<TrainStationIslandEntity>, Iterable<TrainStationIslandEntity>>() {
                    @Override
                    public Iterable<TrainStationIslandEntity> call(List<TrainStationIslandEntity> trainStationIslandEntities) {
                        return trainStationIslandEntities;
                    }
                }).flatMap(new Func1<TrainStationIslandEntity, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(TrainStationIslandEntity trainStationIslandEntity) {
                        return insert(trainStationIslandEntity);
                    }
                });*/
    }


    @Override
    public Observable<List<TrainStation>> getDatas(final Specification specification) {
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
        }).map(new TrainStationDbMapper());
    }

    @Override
    public Observable<Integer> getCount(Specification specification) {
        return null;
    }

    @Override
    public Observable<TrainStation> getData(final Specification specification) {
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
        }).map(new Func1<TrainStationDb, TrainStation>() {
            @Override
            public TrainStation call(TrainStationDb trainStationDb) {
                TrainStationDbMapper mapper = new TrainStationDbMapper();
                return mapper.transform(trainStationDb);
            }
        });
    }
}

