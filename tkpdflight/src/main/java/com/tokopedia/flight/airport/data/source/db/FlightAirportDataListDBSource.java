package com.tokopedia.flight.airport.data.source.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCity;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportDetail;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB_Table;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightAirportDataListDBSource implements DataListDBSource<FlightAirportCountry> {

    @Inject
    public FlightAirportDataListDBSource() {
    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select().from(FlightAirportDB.class).count() > 0);
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(FlightAirportDB.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<Boolean> insertAll(final List<FlightAirportCountry> flightAirportCountryList) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                for (FlightAirportCountry flightAirportCountry : flightAirportCountryList) {
                    for (FlightAirportCity flightAirportCity: flightAirportCountry.getAttributes().getCities()) {
                        for (FlightAirportDetail flightAirportDetail : flightAirportCity.getFlightAirportDetails()) {
                            insertFlight(flightAirportCountry, flightAirportCity, flightAirportDetail);
                        }
                    }
                }
                subscriber.onNext(new Select().from(FlightAirportDB.class).count() > 0);
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
        });
    }

    public Observable<List<FlightAirportDB>> getAirportList(final String queryText) {
        return Observable.create(new Observable.OnSubscribe<List<FlightAirportDB>>() {
            @Override
            public void call(Subscriber<? super List<FlightAirportDB>> subscriber) {
                List<FlightAirportDB> flightAirportDBList = new Select().from(FlightAirportDB.class)
                        .where(FlightAirportDB_Table.country_id.like(queryText))
                        .or(FlightAirportDB_Table.country_name.like(queryText))
                        .or(FlightAirportDB_Table.city_name.like(queryText))
                        .or(FlightAirportDB_Table.city_code.like(queryText))
                        .or(FlightAirportDB_Table.airport_id.like(queryText))
                        .or(FlightAirportDB_Table.airport_name.like(queryText))
                        .or(FlightAirportDB_Table.aliases.like(queryText))
                        .queryList();
                subscriber.onNext(flightAirportDBList);
            }
        });
    }
}
