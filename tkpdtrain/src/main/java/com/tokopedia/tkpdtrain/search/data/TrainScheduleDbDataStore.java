package com.tokopedia.tkpdtrain.search.data;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.tokopedia.tkpdtrain.common.TrainDataDBSource;
import com.tokopedia.tkpdtrain.common.specification.DbFlowSpecification;
import com.tokopedia.tkpdtrain.common.specification.DbFlowWithOrderSpecification;
import com.tokopedia.tkpdtrain.common.specification.Specification;
import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable;
import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.tkpdtrain.search.data.entity.ScheduleAvailabilityEntity;
import com.tokopedia.tkpdtrain.search.data.entity.TrainScheduleEntity;
import com.tokopedia.tkpdtrain.search.data.typedef.TrainAvailabilityTypeDef;
import com.tokopedia.tkpdtrain.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.tkpdtrain.search.domain.mapper.TrainScheduleMapper;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainScheduleDbDataStore implements TrainDataDBSource<TrainScheduleEntity, TrainScheduleViewModel> {

    public TrainScheduleDbDataStore() {
    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(TrainScheduleDbTable.class).hasData());
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(TrainScheduleDbTable.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<Boolean> insert(final TrainScheduleEntity trainScheduleEntity) {
        return null;
    }

    @Override
    public Observable<Boolean> insertAll(final List<TrainScheduleEntity> datas) {
        return null;
    }

    public Observable<Boolean> insertAllData(final List<TrainScheduleEntity> datas, final int scheduleVariant) {
        return Observable.just(datas)
                .map(new Func1<List<TrainScheduleEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<TrainScheduleEntity> trainScheduleEntities) {
                        for (TrainScheduleEntity trainListSchedulesEntity : datas) {
                            insertSchedule(trainListSchedulesEntity, scheduleVariant);
                        }
                        updateFilterCheapest();
                        updateFilterFastest();
                        return true;
                    }
                });
    }

    private void insertSchedule(TrainScheduleEntity trainScheduleEntity, int scheduleVariant) {
        ModelAdapter<TrainScheduleDbTable> adapter = FlowManager.getModelAdapter(TrainScheduleDbTable.class);
        TrainScheduleDbTable trainScheduleDbTable = new TrainScheduleDbTable();
        trainScheduleDbTable.setIdSchedule(trainScheduleEntity.getIdSchedule());
        trainScheduleDbTable.setAdultFare(trainScheduleEntity.getAdultFare());
        trainScheduleDbTable.setDisplayAdultFare(trainScheduleEntity.getDisplayAdultFare());
        trainScheduleDbTable.setInfantFare(trainScheduleEntity.getInfantFare());
        trainScheduleDbTable.setDisplayInfantFare(trainScheduleEntity.getDisplayInfantFare());
        trainScheduleDbTable.setArrivalTimestamp(trainScheduleEntity.getArrivalTimestamp());
        trainScheduleDbTable.setDepartureTimestamp(trainScheduleEntity.getDepartureTimestamp());
        trainScheduleDbTable.setClassTrain(trainScheduleEntity.getClassTrain());
        trainScheduleDbTable.setDisplayClass(trainScheduleEntity.getDisplayClass());
        trainScheduleDbTable.setSubclass(trainScheduleEntity.getSubclass());
        trainScheduleDbTable.setOrigin(trainScheduleEntity.getOrigin());
        trainScheduleDbTable.setDestination(trainScheduleEntity.getDestination());
        trainScheduleDbTable.setDisplayDuration(trainScheduleEntity.getDisplayDuration());
        trainScheduleDbTable.setDuration(trainScheduleEntity.getDuration());
        trainScheduleDbTable.setTrainKey(trainScheduleEntity.getTrainKey());
        trainScheduleDbTable.setTrainName(trainScheduleEntity.getTrainName());
        trainScheduleDbTable.setTrainNumber(trainScheduleEntity.getTrainNumber());
        trainScheduleDbTable.setAvailableSeat(TrainAvailabilityTypeDef.DEFAULT_VALUE);
        trainScheduleDbTable.setCheapestFlag(false);
        trainScheduleDbTable.setFastestFlag(false);
        trainScheduleDbTable.setReturnSchedule(scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE);
        adapter.insert(trainScheduleDbTable);
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> getDatas(final Specification specification) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<TrainScheduleDbTable>>() {
            @Override
            public void call(Subscriber<? super List<TrainScheduleDbTable>> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).getCondition();
                }
                List<OrderBy> orderBies = new ArrayList<>();
                if (specification instanceof DbFlowWithOrderSpecification) {
                    orderBies = ((DbFlowWithOrderSpecification) specification).toOrder();
                }
                List<TrainScheduleDbTable> trainScheduleDBList = new Select()
                        .from(TrainScheduleDbTable.class)
                        .where(conditions)
                        .orderByAll(orderBies)
                        .queryList();

                subscriber.onNext(trainScheduleDBList);
            }
        }).map(new TrainScheduleMapper());
    }

    @Override
    public Observable<Integer> getCount(Specification specification) {
        return null;
    }

    public Observable<Boolean> updateDataAvailability(final List<ScheduleAvailabilityEntity> scheduleAvailabilityEntities) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                for (ScheduleAvailabilityEntity scheduleAvailability : scheduleAvailabilityEntities) {
                    updateAvailability(scheduleAvailability);
                }
                subscriber.onNext(true);
            }

            private void updateAvailability(ScheduleAvailabilityEntity scheduleAvailabilityEntity) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(TrainScheduleDbTable_Table.schedule_id.eq(scheduleAvailabilityEntity.getIdSchedule()));
                TrainScheduleDbTable result = new Select()
                        .from(TrainScheduleDbTable.class)
                        .where(conditions)
                        .querySingle();
                if (result != null) {
                    result.setAvailableSeat(scheduleAvailabilityEntity.getAvailableSeat());
                    result.save();
                }
            }
        });
    }

    private void updateFilterCheapest() {
        TrainScheduleDbTable cheapestSchedule = new Select(Method.ALL_PROPERTY, Method.min(TrainScheduleDbTable_Table.adult_fare))
                .from(TrainScheduleDbTable.class)
                .querySingle();

        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.adult_fare.eq(cheapestSchedule.getAdultFare()));

        List<TrainScheduleDbTable> result = new Select()
                .from(TrainScheduleDbTable.class)
                .where(conditions)
                .queryList();
        for (TrainScheduleDbTable trainScheduleDbTable : result) {
            trainScheduleDbTable.setCheapestFlag(true);
            trainScheduleDbTable.save();
        }
    }

    private void updateFilterFastest() {
        TrainScheduleDbTable fastestSchedule = new Select(Method.ALL_PROPERTY, Method.min(TrainScheduleDbTable_Table.duration))
                .from(TrainScheduleDbTable.class)
                .querySingle();

        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.duration.eq(fastestSchedule.getDuration()));

        List<TrainScheduleDbTable> result = new Select()
                .from(TrainScheduleDbTable.class)
                .where(conditions)
                .queryList();
        for (TrainScheduleDbTable trainScheduleDbTable : result) {
            trainScheduleDbTable.setFastestFlag(true);
            trainScheduleDbTable.save();
        }
    }

    @Override
    public Observable<TrainScheduleViewModel> getData(final Specification specification) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<TrainScheduleDbTable>() {
            @Override
            public void call(Subscriber<? super TrainScheduleDbTable> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).getCondition();
                }
                List<OrderBy> orderBies = new ArrayList<>();
                if (specification instanceof DbFlowWithOrderSpecification) {
                    orderBies = ((DbFlowWithOrderSpecification) specification).toOrder();
                }
                TrainScheduleDbTable trainScheduleDBList = new Select()
                        .from(TrainScheduleDbTable.class)
                        .where(conditions)
                        .orderByAll(orderBies)
                        .querySingle();

                subscriber.onNext(trainScheduleDBList);
            }
        }).map(new Func1<TrainScheduleDbTable, TrainScheduleViewModel>() {
            @Override
            public TrainScheduleViewModel call(TrainScheduleDbTable trainScheduleDbTable) {
                TrainScheduleMapper mapper = new TrainScheduleMapper();
                return mapper.transform(trainScheduleDbTable);
            }
        });
    }
}
