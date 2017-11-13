package com.tokopedia.flight.search.data.db;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.tokopedia.flight.common.data.db.BaseDataListDBSource;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.filter.DepartureTimeEnum;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;
import com.tokopedia.flight.search.view.model.filter.TransitEnum;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public abstract class AbsFlightSearchDataListDBSource extends BaseDataListDBSource<FlightSearchData, FlightSearchSingleRouteDB> {

    protected abstract Class<? extends FlightSearchSingleRouteDB> getDBClass();

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
                .onErrorReturn(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        return false;
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
                List<? extends FlightSearchSingleRouteDB> flightSearchSingleRouteDBList;
                if (flightFilterModel == null) {
                    flightSearchSingleRouteDBList = new Select().from(getDBClass())
                            .queryList();
                } else {
                    flightSearchSingleRouteDBList = new Select().from(getDBClass())
                            .where(getSQLCondition(flightFilterModel))
                            .queryList();
                }
                subscriber.onNext((List<FlightSearchSingleRouteDB>) flightSearchSingleRouteDBList);
            }
        });
    }

    @Override
    public Observable<Integer> getDataCount(final HashMap<String, Object> params) {
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

    private ConditionGroup getSQLCondition(FlightFilterModel flightFilterModel) {
        ConditionGroup conditionGroup = ConditionGroup.clause()
                .and(getTotalNumericColumn().between(flightFilterModel.getPriceMin()).and(flightFilterModel.getPriceMax()))
                .and(getDurationColumn().between(flightFilterModel.getDurationMin()).and(flightFilterModel.getDurationMax()));
        List<TransitEnum> transitEnumList = flightFilterModel.getTransitTypeList();
        ConditionGroup transitConditionFroup = getTransitCondition(transitEnumList);
        if (transitConditionFroup != null) {
            conditionGroup.and(transitConditionFroup);
        }

        List<String> airlineList = flightFilterModel.getAirlineList();
        ConditionGroup airlineConditionGroup = getAirlineCondition(airlineList);
        if (airlineConditionGroup != null) {
            conditionGroup.and(airlineConditionGroup);
        }

        List<DepartureTimeEnum> departureTimeEnumList = flightFilterModel.getDepartureTimeList();
        ConditionGroup departureTimeCondition = getDepartureTimeCondition(departureTimeEnumList);
        if (departureTimeCondition != null) {
            conditionGroup.and(departureTimeCondition);
        }

        List<RefundableEnum> refundableEnumList = flightFilterModel.getRefundableTypeList();
        ConditionGroup refundableEnumCondition = getRefundableCondition(refundableEnumList);
        if (refundableEnumCondition != null) {
            conditionGroup.and(refundableEnumCondition);
        }

        return conditionGroup;
    }

    private ConditionGroup getTransitCondition(List<TransitEnum> transitEnumList) {
        if (transitEnumList == null || transitEnumList.size() == 0) {
            return null;
        }
        ConditionGroup transitConditionGroup = ConditionGroup.clause();
        for (int i = 0, sizei = transitEnumList.size(); i < sizei; i++) {
            TransitEnum transitEnum = transitEnumList.get(i);
            switch (transitEnum) {
                case DIRECT:
                    transitConditionGroup.or(getTotalTransit().eq(0));
                    break;
                case ONE:
                    transitConditionGroup.or(getTotalTransit().eq(1));
                    break;
                case TWO:
                    transitConditionGroup.or(getTotalTransit().eq(2));
                    break;
                case THREE_OR_MORE:
                    transitConditionGroup.or(getTotalTransit().greaterThanOrEq(3));
                    break;
            }
        }
        return transitConditionGroup;
    }

    private ConditionGroup getDepartureTimeCondition(List<DepartureTimeEnum> departureTimeEnumList) {
        if (departureTimeEnumList == null || departureTimeEnumList.size() == 0) {
            return null;
        }
        ConditionGroup departureConditionGroup = ConditionGroup.clause();
        for (int i = 0, sizei = departureTimeEnumList.size(); i < sizei; i++) {
            DepartureTimeEnum departureTimeEnum = departureTimeEnumList.get(i);
            switch (departureTimeEnum) {
                case _00:
                    departureConditionGroup.or(getDepartureTimeColumn().between(0).and(559));
                    break;
                case _06:
                    departureConditionGroup.or(getDepartureTimeColumn().between(600).and(1159));
                    break;
                case _12:
                    departureConditionGroup.or(getDepartureTimeColumn().between(1200).and(1759));
                    break;
                case _18:
                    departureConditionGroup.or(getDepartureTimeColumn().between(1800).and(2359));
                    break;
            }
        }
        return departureConditionGroup;
    }

    private ConditionGroup getRefundableCondition(List<RefundableEnum> refundableEnumList) {
        if (refundableEnumList == null || refundableEnumList.size() == 0) {
            return null;
        }
        ConditionGroup refundableContionGroup = ConditionGroup.clause();
        for (int i = 0, sizei = refundableEnumList.size(); i < sizei; i++) {
            RefundableEnum refundableEnum = refundableEnumList.get(i);
            switch (refundableEnum) {
                case REFUNDABLE:
                    refundableContionGroup.or(getRefundableColumn().eq(true));
                    break;
                case NOT_REFUNDABLE:
                    refundableContionGroup.or(getRefundableColumn().eq(false));
                    break;
            }
        }
        return refundableContionGroup;
    }

    private ConditionGroup getAirlineCondition(List<String> airlineList) {
        if (airlineList == null || airlineList.size() == 0) {
            return null;
        }
        ConditionGroup airlineConditionGroup = ConditionGroup.clause();
        for (int i = 0, sizei = airlineList.size(); i < sizei; i++) {
            String airline = airlineList.get(i);
            airlineConditionGroup.or(getAirlineColumn().like("%"+airline+"%"));
        }
        return airlineConditionGroup;
    }

    private IntProperty getTotalNumericColumn() {
        return new IntProperty(getDBClass(), FlightSearchSingleRouteDB.TOTAL_NUMERIC);
    }

    private IntProperty getDurationColumn() {
        return new IntProperty(getDBClass(), FlightSearchSingleRouteDB.DURATION_MINUTE);
    }

    private IntProperty getTotalTransit() {
        return new IntProperty(getDBClass(), FlightSearchSingleRouteDB.TOTAL_TRANSIT);
    }
    private Property<String> getAirlineColumn() {
        return new Property<>(getDBClass(), FlightSearchSingleRouteDB.AIRLINE);
    }
    private IntProperty getDepartureTimeColumn() {
        return new IntProperty(getDBClass(), FlightSearchSingleRouteDB.DEPARTURE_TIME_INT);
    }

    private Property<Boolean> getRefundableColumn() {
        return new Property<>(getDBClass(), FlightSearchSingleRouteDB.IS_REFUNDABLE);
    }

    private Property<String> getPrimaryColumn() {
        return new Property<String>(getDBClass(), FlightSearchSingleRouteDB.ID);
    }


    private ConditionGroup getFindByPrimaryKeyQuery(String primaryKey) {
        return ConditionGroup.clause()
                .and(getPrimaryColumn().eq(primaryKey));
    }


    public Observable<FlightSearchSingleRouteDB> find(final String primaryKey) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<FlightSearchSingleRouteDB>() {
            @Override
            public void call(Subscriber<? super FlightSearchSingleRouteDB> subscriber) {
                FlightSearchSingleRouteDB flightSearchSingleRouteDBList = new Select().from(getDBClass())
                        .where(getFindByPrimaryKeyQuery(primaryKey))
                        .querySingle();
                subscriber.onNext(flightSearchSingleRouteDBList);
            }
        });
    }
}
