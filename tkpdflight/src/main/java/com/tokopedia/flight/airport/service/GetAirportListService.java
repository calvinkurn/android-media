package com.tokopedia.flight.airport.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportModule;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public class GetAirportListService extends IntentService implements FlightAirportPickerBackgroundContract.View {

    private static final String ARG_EXTRA_GET_AIRPORT = "ARG_EXTRA_GET_AIRPORT";
    private static final int CODE_EXTRA_GET_AIRPORT = 432;

    public GetAirportListService() {
        super(GetAirportListService.class.getCanonicalName());
    }

    @Inject
    FlightAirportPickerBackgroundPresenter flightAirportPickerBackgroundPresenter;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int code = intent.getIntExtra(ARG_EXTRA_GET_AIRPORT, 0);
        switch (code) {
            case CODE_EXTRA_GET_AIRPORT:
                flightAirportPickerBackgroundPresenter.getAirportListCloud();
                break;
        }
    }

    @Override
    public void onCreate() {
        DaggerFlightAirportComponent
                .builder()
                .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                .flightAirportModule(new FlightAirportModule())
                .build().inject(this);
        flightAirportPickerBackgroundPresenter.attachView(this);
        super.onCreate();
    }

    @Override
    public void onGetAirportError(Throwable e) {

    }

    @Override
    public void onGetAirport(Boolean isSuccess) {

    }
}
