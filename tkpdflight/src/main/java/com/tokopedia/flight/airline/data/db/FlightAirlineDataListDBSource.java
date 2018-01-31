package com.tokopedia.flight.airline.data.db;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.Model;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB_Table;
import com.tokopedia.flight.airline.util.FlightAirlineParamUtil;
import com.tokopedia.flight.common.data.db.BaseDataListDBSource;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightAirlineDataListDBSource extends BaseDataListDBSource<AirlineData,FlightAirlineDB> {

    @Inject
    public FlightAirlineDataListDBSource() {
    }

    @Override
    protected Class<? extends Model> getDBClass() {
        return FlightAirlineDB.class;
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


    public Observable<Boolean> insert(AirlineData airlineData) {
        return Observable.just(airlineData)
                .map(new Func1<AirlineData, Boolean>() {
                    @Override
                    public Boolean call(AirlineData airlineData) {
                        FlightAirlineDB flightAirlineDB = new FlightAirlineDB(airlineData);
                        flightAirlineDB.insert();
                        return true ;
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

    public Observable<FlightAirlineDB> getAirline(String airlineId) {
        final String queryLike = "%" + airlineId + "%";
        return Observable.unsafeCreate(new Observable.OnSubscribe<FlightAirlineDB>() {
            @Override
            public void call(Subscriber<? super FlightAirlineDB> subscriber) {
                FlightAirlineDB flightAirportDBList = new Select()
                        .from(FlightAirlineDB.class)
                        .where(FlightAirlineDB_Table.id.like(queryLike))
                        .querySingle();
                subscriber.onNext(flightAirportDBList);
            }
        });
    }

}
