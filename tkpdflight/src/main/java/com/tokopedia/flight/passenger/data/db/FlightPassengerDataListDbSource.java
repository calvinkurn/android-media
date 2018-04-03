package com.tokopedia.flight.passenger.data.db;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.Model;
import com.tokopedia.flight.common.data.db.BaseDataListDBSource;
import com.tokopedia.flight.passenger.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;
import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb_Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author by furqan on 28/02/18.
 */

public class FlightPassengerDataListDbSource extends BaseDataListDBSource<SavedPassengerEntity, FlightPassengerDb> {

    public static final String PASSENGER_ID = "PASSENGER_ID";

    @Inject
    public FlightPassengerDataListDbSource() {
    }

    @Override
    protected Class<? extends Model> getDBClass() {
        return FlightPassengerDb.class;
    }

    @Override
    public Observable<Boolean> insertAll(List<SavedPassengerEntity> list) {
        return Observable.from(list)
                .flatMap(new Func1<SavedPassengerEntity, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(SavedPassengerEntity savedPassengerEntity) {
                        FlightPassengerDb flightPassengerDb = new FlightPassengerDb(savedPassengerEntity);
                        flightPassengerDb.insert();
                        return Observable.just(true);
                    }
                })
                .toList()
                .flatMap(new Func1<List<Boolean>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<Boolean> booleans) {
                        return Observable.just(new Select(Method.count())
                                .from(FlightPassengerDb.class)
                                .hasData());
                    }
                });
    }

    public Observable<Boolean> updateIsSelected(final String passengerId, final int isSelected) {

        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(FlightPassengerDb_Table.id.eq(passengerId));

                FlightPassengerDb result = new Select().from(FlightPassengerDb.class)
                        .where(conditions)
                        .querySingle();

                if (result != null) {
                    result.setIsSelected(isSelected);
                    result.save();
                    subscriber.onNext(true);
                } else {
                    subscriber.onNext(false);
                }
            }
        });
    }

    public Observable<Boolean> updatePassengerData(final UpdatePassengerRequest updatePassengerRequest) {

        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(FlightPassengerDb_Table.id.eq(updatePassengerRequest.getPassengerId()));

                FlightPassengerDb result = new Select().from(FlightPassengerDb.class)
                        .where(conditions)
                        .querySingle();

                if (result != null) {
                    result.setFirstName(updatePassengerRequest.getAttributes().getFirstName());
                    result.setLastName(updatePassengerRequest.getAttributes().getLastName());
                    result.setBirthdate(updatePassengerRequest.getAttributes().getDob());
                    result.setTitleId(updatePassengerRequest.getAttributes().getTitle());
                    result.save();
                    subscriber.onNext(true);
                } else {
                    subscriber.onNext(false);
                }
            }
        });
    }

    @Override
    public Observable<List<FlightPassengerDb>> getData(HashMap<String, Object> params) {
        final ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(FlightPassengerDb_Table.is_selected.eq(0));

        if (params != null &&
                params.containsKey(PASSENGER_ID) &&
                !params.get(PASSENGER_ID).equals("")) {
            conditions.or(FlightPassengerDb_Table.id.eq((String) params.get(PASSENGER_ID)));
        }

        final List<OrderBy> orderByList = new ArrayList<>();
        orderByList.add(OrderBy.fromProperty(FlightPassengerDb_Table.first_name).ascending());
        orderByList.add(OrderBy.fromProperty(FlightPassengerDb_Table.last_name).ascending());

        return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightPassengerDb>>() {
            @Override
            public void call(Subscriber<? super List<FlightPassengerDb>> subscriber) {
                List<FlightPassengerDb> flightPassengerDbList;

                flightPassengerDbList = new Select().from(FlightPassengerDb.class)
                        .where(conditions)
                        .orderByAll(orderByList)
                        .queryList();
                subscriber.onNext(flightPassengerDbList);
            }
        });
    }

    @Override
    public Observable<Integer> getDataCount(HashMap<String, Object> params) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                long count = new Select(Method.count())
                        .from(getDBClass())
                        .count();
                subscriber.onNext((int) count);
            }
        });
    }

    public Observable<FlightPassengerDb> getSingleData(String passengerId) {
        final ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(FlightPassengerDb_Table.id.eq(passengerId));

        return Observable.unsafeCreate(new Observable.OnSubscribe<FlightPassengerDb>() {
            @Override
            public void call(Subscriber<? super FlightPassengerDb> subscriber) {
                FlightPassengerDb flightPassengerDb;

                flightPassengerDb = new Select().from(FlightPassengerDb.class)
                        .where(conditions)
                        .querySingle();

                subscriber.onNext(flightPassengerDb);
            }
        });
    }
}
