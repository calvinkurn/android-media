package com.tokopedia.flight.airport.data.source.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCity;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportDetail;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB_Table;

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

public class FlightAirportDataListDBSource implements DataListDBSource<FlightAirportCountry,FlightAirportDB> {

    @Inject
    public FlightAirportDataListDBSource() {
    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(FlightAirportDB.class).hasData());
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(FlightAirportDB.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<Boolean> insertAll(final List<FlightAirportCountry> flightAirportCountryList) {
        return Observable.from(flightAirportCountryList)
                .flatMap(new Func1<FlightAirportCountry, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(FlightAirportCountry flightAirportCountry) {
                        for (FlightAirportCity flightAirportCity: flightAirportCountry.getAttributes().getCities()) {
                            if(flightAirportCity.getFlightAirportDetails() != null && flightAirportCity.getFlightAirportDetails().size() > 1){
                                insertFlight(flightAirportCountry, flightAirportCity, new FlightAirportDetail("", "", new ArrayList<String>()));
                            }
                            for (FlightAirportDetail flightAirportDetail : flightAirportCity.getFlightAirportDetails()) {
                                insertFlight(flightAirportCountry, flightAirportCity, flightAirportDetail);
                            }
                        }
                        return Observable.just(true);
                    }

                    private void insertFlight(FlightAirportCountry flightAirportCountry, FlightAirportCity flightAirportCity, FlightAirportDetail flightAirportDetail) {
                        FlightAirportDB flightAirportDB = new FlightAirportDB();
                        flightAirportDB.setCountryId(flightAirportCountry.getId());
                        flightAirportDB.setCountryName(flightAirportCountry.getAttributes().getName());
                        flightAirportDB.setPhoneCode(flightAirportCountry.getAttributes().getPhoneCode());
                        flightAirportDB.setCityId(flightAirportCity.getId());
                        flightAirportDB.setCityName(flightAirportCity.getName());
                        flightAirportDB.setCityCode(flightAirportCity.getCode());
                        flightAirportDB.setAirportId(flightAirportDetail.getId());
                        flightAirportDB.setAirportName(flightAirportDetail.getName());
                        String aliases = "";
                        for (String alias: flightAirportDetail.getAliases()) {
                            aliases += alias;
                        }
                        flightAirportDB.setAliases(aliases);
                        flightAirportDB.insert();
                    }
                })
                .toList()
                .flatMap(new Func1<List<Boolean>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<Boolean> booleen) {
                        return Observable.just(new Select(Method.count()).from(FlightAirportDB.class).hasData());
                    }
                });

    }

    @Override
    public Observable<List<FlightAirportDB>> getData(HashMap<String, Object> params) {
        final String queryText = FlightAirportDataListSource.getQueryFromMap(params);
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightAirportDB>>() {
            @Override
            public void call(Subscriber<? super List<FlightAirportDB>> subscriber) {
                String queryLike = "%" + queryText + "%";
                List<FlightAirportDB> flightAirportDBList = new Select().from(FlightAirportDB.class)
                        .where(FlightAirportDB_Table.country_id.like(queryLike))
                        .or(FlightAirportDB_Table.country_name.like(queryLike))
                        .or(FlightAirportDB_Table.city_name.like(queryLike))
                        .or(FlightAirportDB_Table.city_code.like(queryLike))
                        .or(FlightAirportDB_Table.airport_id.like(queryLike))
                        .or(FlightAirportDB_Table.airport_name.like(queryLike))
                        .or(FlightAirportDB_Table.aliases.like(queryLike))
                        .queryList();
                subscriber.onNext(flightAirportDBList);
            }
        });
    }

    @Override
    public Observable<Integer> getDataCount(HashMap<String, Object> params) {
        return getDataCount(params).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                long count = new Select(Method.count()).from(FlightAirportDB.class).count();
                return Observable.just((int)count);
            }
        });
    }

}
