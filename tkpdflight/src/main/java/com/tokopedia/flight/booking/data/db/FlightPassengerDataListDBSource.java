package com.tokopedia.flight.booking.data.db;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.Model;
import com.tokopedia.flight.booking.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.booking.data.db.model.FlightPassengerDB;
import com.tokopedia.flight.booking.data.db.model.FlightPassengerDB_Table;
import com.tokopedia.flight.common.data.db.BaseDataListDBSource;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author by furqan on 28/02/18.
 */

public class FlightPassengerDataListDBSource extends BaseDataListDBSource<SavedPassengerEntity, FlightPassengerDB> {

    public static final String PASSENGER_ID = "PASSENGER_ID";

    @Inject
    public FlightPassengerDataListDBSource() {
    }

    @Override
    protected Class<? extends Model> getDBClass() {
        return FlightPassengerDB.class;
    }

    @Override
    public Observable<Boolean> insertAll(List<SavedPassengerEntity> list) {
        return Observable.from(list)
                .flatMap(new Func1<SavedPassengerEntity, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(SavedPassengerEntity savedPassengerEntity) {
                        FlightPassengerDB flightPassengerDB = new FlightPassengerDB(savedPassengerEntity);
                        flightPassengerDB.insert();
                        return Observable.just(true);
                    }
                })
                .toList()
                .flatMap(new Func1<List<Boolean>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<Boolean> booleans) {
                        return Observable.just(new Select(Method.count())
                                .from(FlightPassengerDB.class)
                                .hasData());
                    }
                });
    }

    public Observable<Boolean> updateIsSelected(final String passengerId, final int isSelected) {

        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(FlightPassengerDB_Table.id.eq(passengerId));

                FlightPassengerDB result = new Select().from(FlightPassengerDB.class)
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

    @Override
    public Observable<List<FlightPassengerDB>> getData(HashMap<String, Object> params) {
        final ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(FlightPassengerDB_Table.is_selected.eq(0));

        if (params != null &&
                params.containsKey(PASSENGER_ID) &&
                !params.get(PASSENGER_ID).equals("")) {
            conditions.or(FlightPassengerDB_Table.id.eq((String) params.get(PASSENGER_ID)));
        }

        return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightPassengerDB>>() {
            @Override
            public void call(Subscriber<? super List<FlightPassengerDB>> subscriber) {
                List<FlightPassengerDB> flightPassengerDBList;

                flightPassengerDBList = new Select().from(FlightPassengerDB.class)
                        .where(conditions)
                        .queryList();
                subscriber.onNext(flightPassengerDBList);
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
}
