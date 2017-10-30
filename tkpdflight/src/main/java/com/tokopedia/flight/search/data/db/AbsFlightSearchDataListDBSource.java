package com.tokopedia.flight.search.data.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public abstract class AbsFlightSearchDataListDBSource implements DataListDBSource<FlightSearchData,FlightSearchSingleRouteDB> {

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(getDBClass()).hasData());
            }
        });
    }

    protected abstract Class<? extends  FlightSearchSingleRouteDB> getDBClass();

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(getDBClass()).execute();
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<Boolean> insertAll(final List<FlightSearchData> flightSearchDataList) {
        return Observable.from(flightSearchDataList)
                .flatMap(new Func1<FlightSearchData, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(FlightSearchData flightSearchData) {
                        FlightSearchSingleRouteDB flightSearchSingleRouteDB = new FlightSearchSingleRouteDB(flightSearchData);
                        flightSearchSingleRouteDB.insert();
                        return Observable.just(true);
                    }
                })
                .toList()
                .flatMap(new Func1<List<Boolean>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<Boolean> booleen) {
                        return Observable.just(new Select(Method.count()).from(getDBClass()).hasData());
                    }
                });

    }

    @Override
    public Observable<List<FlightSearchSingleRouteDB>> getData(HashMap<String, Object> params) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightSearchSingleRouteDB>>() {
            @Override
            public void call(Subscriber<? super List<FlightSearchSingleRouteDB>> subscriber) {
                List<? extends FlightSearchSingleRouteDB> flightSearchSingleRouteDBList = new Select().from(getDBClass()).queryList();
                subscriber.onNext((List<FlightSearchSingleRouteDB>) flightSearchSingleRouteDBList);
            }
        });
    }
}
