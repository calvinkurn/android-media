package com.tokopedia.flight.airline.data.db;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB_Table;
import com.tokopedia.flight.airline.util.FlightAirlineParamUtil;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightAirlineDataListDBSource implements DataListDBSource<AirlineData,FlightAirlineDB> {

    @Inject
    public FlightAirlineDataListDBSource() {
    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(FlightAirlineDB.class).hasData());
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(FlightAirlineDB.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<Boolean> insertAll(final List<AirlineData> airlineDataList) {
        return Observable.from(airlineDataList)
                .flatMap(new Func1<AirlineData, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(AirlineData airlineData) {
                        FlightAirlineDB flightAirlineDB = new FlightAirlineDB(airlineData);
                        flightAirlineDB.insert();
                        return Observable.just(true);
                    }
                })
                .toList()
                .flatMap(new Func1<List<Boolean>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<Boolean> booleen) {
                        return Observable.just(new Select(Method.count()).from(FlightAirlineDB.class).hasData());
                    }
                });

    }

    @Override
    public Observable<List<FlightAirlineDB>> getData(HashMap<String, Object> params) {
        final String id = FlightAirlineParamUtil.getId(params);
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightAirlineDB>>() {
            @Override
            public void call(Subscriber<? super List<FlightAirlineDB>> subscriber) {
                List<FlightAirlineDB> flightAirlineDBs;
                if (TextUtils.isEmpty(id)) {
                    flightAirlineDBs = new Select().from(FlightAirlineDB.class).queryList();
                } else {
                    flightAirlineDBs = new Select().from(FlightAirlineDB.class)
                            .where(FlightAirlineDB_Table.id.eq(id))
                            .queryList();
                }
                subscriber.onNext(flightAirlineDBs);
            }
        });
    }

    @Override
    public Observable<Integer> getDataCount(HashMap<String, Object> params) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                long count = new Select(Method.count()).from(FlightAirlineDB.class).count();
                subscriber.onNext((int) count);
            }
        });
    }
}
