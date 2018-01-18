package com.tokopedia.flight.search.data.db;

import android.text.TextUtils;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;
import com.tokopedia.flight.common.data.db.BaseDataDBSource;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.cloud.model.response.Meta;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB_Table;
import com.tokopedia.flight.search.data.db.model.FlightSearchReturnRouteDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchMetaParamUtil;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.filter.DepartureTimeEnum;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;
import com.tokopedia.flight.search.view.model.filter.TransitEnum;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightMetaDataDBSource
        extends BaseDataDBSource<Meta, List<FlightMetaDataDB>> {

    @Inject
    public FlightMetaDataDBSource(){

    }

    @Override
    protected Class<? extends FlightMetaDataDB> getDBClass() {
        return FlightMetaDataDB.class;
    }

    @Override
    public Observable<Boolean> insertAll(final Meta meta) {
        return Observable.just(meta).flatMap(new Func1<Meta, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Meta meta) {
                        insertFlightMetaData(meta);
                        return Observable.just(true);
                    }
                })
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean booleen) {
                        return Observable.just(new Select(Method.count()).from(getDBClass()).hasData());
                    }
                });
    }

    protected void insertFlightMetaData(Meta meta) {
        FlightMetaDataDB flightMetaDataDB = new FlightMetaDataDB(meta);
        flightMetaDataDB.insert();
    }

    @Override
    public Observable<List<FlightMetaDataDB>> getData(HashMap<String, Object> params) {
        final String departure= FlightSearchMetaParamUtil.getDeparture(params);
        final String arrival= FlightSearchMetaParamUtil.getArrival(params);
        final String date= FlightSearchMetaParamUtil.getDate(params);
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightMetaDataDB>>() {
            @Override
            public void call(Subscriber<? super List<FlightMetaDataDB>> subscriber) {
                List<? extends FlightMetaDataDB> flightMetaDataDBs;
                if (TextUtils.isEmpty(departure) && TextUtils.isEmpty(arrival) && TextUtils.isEmpty(date)) {
                    flightMetaDataDBs = new Select().from(getDBClass()).queryList();
                } else {
                    flightMetaDataDBs = new Select().from(getDBClass())
                            .where(getConditionGroup(arrival, departure, date))
                            .queryList();
                }
                subscriber.onNext((List<FlightMetaDataDB>)flightMetaDataDBs);
            }
        });
    }

    private ConditionGroup getConditionGroup(String arrival, String departure, String date){
        return ConditionGroup.clause()
                .and(FlightMetaDataDB_Table.arrival_airport.eq(arrival))
                .and(FlightMetaDataDB_Table.departure_airport.eq(departure))
                .and(FlightMetaDataDB_Table.date.eq(date));
    }

    @Override
    public Observable<Integer> getDataCount(HashMap<String, Object> params) {
        final String departure= FlightSearchMetaParamUtil.getDeparture(params);
        final String arrival= FlightSearchMetaParamUtil.getArrival(params);
        final String date= FlightSearchMetaParamUtil.getDate(params);
        return Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                long dataCount;
                if (TextUtils.isEmpty(departure) && TextUtils.isEmpty(arrival) && TextUtils.isEmpty(date)) {
                    dataCount = new Select().from(getDBClass()).count();
                } else {
                    dataCount = new Select().from(getDBClass())
                            .where(getConditionGroup(arrival, departure, date))
                            .count();
                }
                subscriber.onNext((int) dataCount);
            }
        });
    }

}
