package com.tokopedia.flight.search.data.db;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.tokopedia.flight.common.data.db.BaseDataListDBSource;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public abstract class AbsFlightSearchDataListDBSource extends BaseDataListDBSource<FlightSearchData,FlightSearchSingleRouteDB> {

    private com.raizlabs.android.dbflow.sql.language.SQLCondition SQLCondition;

    protected abstract Class<? extends  FlightSearchSingleRouteDB> getDBClass();

    @Override
    public Observable<Boolean> insertAll(final List<FlightSearchData> flightSearchDataList) {
        return Observable.from(flightSearchDataList)
                .flatMap(new Func1<FlightSearchData, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(FlightSearchData flightSearchData) {
                        insertSingleFlightData(flightSearchData);
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

    protected abstract void insertSingleFlightData(FlightSearchData flightSearchData);

    @Override
    public Observable<List<FlightSearchSingleRouteDB>> getData(HashMap<String, Object> params) {
        final FlightFilterModel flightFilterModel = FlightSearchParamUtil.getFilterModel(params);
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightSearchSingleRouteDB>>() {
            @Override
            public void call(Subscriber<? super List<FlightSearchSingleRouteDB>> subscriber) {
                List<? extends FlightSearchSingleRouteDB> flightSearchSingleRouteDBList = new Select().from(getDBClass())
                        .where(getSQLCondition(flightFilterModel))
                        .queryList();
                subscriber.onNext((List<FlightSearchSingleRouteDB>) flightSearchSingleRouteDBList);
            }
        });
    }

    @Override
    public Observable<Integer> getDataCount(final HashMap<String, Object> params) {
        // TODO use param to filter
        final FlightFilterModel flightFilterModel = FlightSearchParamUtil.getFilterModel(params);
        return Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                long count = new Select(Method.count()).from(getDBClass())
                        .where(getSQLCondition(flightFilterModel))
                        .count();
                subscriber.onNext((int) count);
            }
        });
    }

    protected ConditionGroup getSQLCondition(FlightFilterModel flightFilterModel) {
        ConditionGroup conditionGroup = ConditionGroup.clause()
                .and(getTotalNumericColumn().between(flightFilterModel.getPriceMin()).and(flightFilterModel.getPriceMax()))
                .and(getDurationColumn().between(flightFilterModel.getDurationMin()).and(flightFilterModel.getDurationMax()));
        return conditionGroup;
    }

    protected IntProperty getTotalNumericColumn(){
        return new IntProperty(getDBClass(), FlightSearchSingleRouteDB.TOTAL_NUMERIC);
    }

    protected IntProperty getDurationColumn(){
        return new IntProperty(getDBClass(), FlightSearchSingleRouteDB.DURATION_MINUTE);
    }
}
