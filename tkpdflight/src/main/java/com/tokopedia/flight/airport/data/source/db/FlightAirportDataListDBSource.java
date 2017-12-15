package com.tokopedia.flight.airport.data.source.db;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.Model;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCity;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportDetail;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB_Table;
import com.tokopedia.flight.common.data.db.BaseDataListDBSource;

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

public class FlightAirportDataListDBSource extends BaseDataListDBSource<FlightAirportCountry, FlightAirportDB> {

    public static final String ID = "id";

    @Inject
    public FlightAirportDataListDBSource() {
    }

    @Override
    protected Class<? extends Model> getDBClass() {
        return FlightAirportDB.class;
    }

    @Override
    public Observable<Boolean> insertAll(final List<FlightAirportCountry> flightAirportCountryList) {
        return Observable.from(flightAirportCountryList)
                .flatMap(new Func1<FlightAirportCountry, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(FlightAirportCountry flightAirportCountry) {
                        if(flightAirportCountry.getAttributes().getCities().size() > 0) {
                            for (FlightAirportCity flightAirportCity : flightAirportCountry.getAttributes().getCities()) {
                                if (flightAirportCity.getFlightAirportDetails() != null && flightAirportCity.getFlightAirportDetails().size() > 1) {
                                    List<String> airportIds = new ArrayList<>();
                                    for (FlightAirportDetail flightAirportDetail : flightAirportCity.getFlightAirportDetails()) {
                                        airportIds.add(flightAirportDetail.getId());
                                    }
                                    insertFlight(flightAirportCountry, flightAirportCity, new FlightAirportDetail("", "", new ArrayList<String>()), TextUtils.join(",", airportIds));
                                }
                                for (FlightAirportDetail flightAirportDetail : flightAirportCity.getFlightAirportDetails()) {
                                    insertFlight(flightAirportCountry, flightAirportCity, flightAirportDetail, "");
                                }
                            }
                        }else{
                            FlightAirportDB flightAirportDB = new FlightAirportDB();
                            flightAirportDB.setCountryId(flightAirportCountry.getId());
                            flightAirportDB.setCityId("");
                            flightAirportDB.setAirportId("");
                            flightAirportDB.setPhoneCode(flightAirportCountry.getAttributes().getPhoneCode());
                            flightAirportDB.setCountryName(flightAirportCountry.getAttributes().getName());
                            insertFlight(flightAirportDB);
                        }
                        return Observable.just(true);
                    }

                    private void insertFlight(FlightAirportDB flightAirportDB) {
                        flightAirportDB.insert();
                    }

                    private void insertFlight(FlightAirportCountry flightAirportCountry, FlightAirportCity flightAirportCity, FlightAirportDetail flightAirportDetail, String airportIds) {
                        FlightAirportDB flightAirportDB = new FlightAirportDB();
                        flightAirportDB.setCountryId(flightAirportCountry.getId());
                        flightAirportDB.setCountryName(flightAirportCountry.getAttributes().getName());
                        flightAirportDB.setPhoneCode(flightAirportCountry.getAttributes().getPhoneCode());
                        flightAirportDB.setCityId(flightAirportCity.getId());
                        flightAirportDB.setCityName(flightAirportCity.getName());
                        flightAirportDB.setCityCode(flightAirportCity.getCode());
                        flightAirportDB.setAirportId(flightAirportDetail.getId());
                        flightAirportDB.setAirportName(flightAirportDetail.getName());
                        flightAirportDB.setAirportIds(airportIds);
                        String aliases = "";
                        for (String alias : flightAirportDetail.getAliases()) {
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
        final String id = FlightAirportDataListSource.getIDFromMap(params);
        if (TextUtils.isEmpty(id)) {
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
                            .orderBy(FlightAirportDB_Table.country_name, true)
                            .orderBy(FlightAirportDB_Table.city_name, true)
                            .orderBy(FlightAirportDB_Table.airport_id, true)
                            .queryList();
                    subscriber.onNext(flightAirportDBList);
                }
            });
        } else {
            return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightAirportDB>>() {
                @Override
                public void call(Subscriber<? super List<FlightAirportDB>> subscriber) {
                    List<FlightAirportDB> flightAirportDBList = new Select().from(FlightAirportDB.class)
                            .where(FlightAirportDB_Table.airport_id.like(id))
                            .queryList();
                    subscriber.onNext(flightAirportDBList);
                }
            });
        }
    }

    @Override
    public Observable<Integer> getDataCount(HashMap<String, Object> params) {
        // TODO use param to filter
        return Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                long count = new Select(Method.count()).from(getDBClass()).count();
                subscriber.onNext((int) count);
            }
        });
    }

}
