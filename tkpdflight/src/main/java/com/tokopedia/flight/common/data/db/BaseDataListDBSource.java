package com.tokopedia.flight.common.data.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.Model;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by User on 11/2/2017.
 */

public abstract class BaseDataListDBSource<T,U> implements DataListDBSource<T,U> {
    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(getDBClass()).hasData());
            }
        });
    }

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

    protected abstract Class<? extends Model> getDBClass();

    @Override
    public abstract Observable<Boolean> insertAll(List<T> list);

    @Override
    public abstract Observable<List<U>> getData(HashMap<String, Object> params);

    @Override
    public abstract Observable<Integer> getDataCount(HashMap<String, Object> params);
}
