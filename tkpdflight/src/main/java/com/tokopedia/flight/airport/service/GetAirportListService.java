package com.tokopedia.flight.airport.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.TkpdFlight;
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportModule;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public class GetAirportListService extends IntentService implements FlightAirportPickerBackgroundContract.View {

    private static final String ARG_EXTRA_GET_AIRPORT = "ARG_EXTRA_GET_AIRPORT";
    private static final int CODE_EXTRA_GET_AIRPORT = 432;
    private static final String ARG_EXTRA_VERSION_AIRPORT = "ARG_EXTRA_VERSION_AIRPORT";

    public GetAirportListService() {
        super(GetAirportListService.class.getCanonicalName());
    }

    @Inject
    FlightAirportPickerBackgroundPresenter flightAirportPickerBackgroundPresenter;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int code = intent.getIntExtra(ARG_EXTRA_GET_AIRPORT, 0);
        long versionAirport = intent.getLongExtra(ARG_EXTRA_VERSION_AIRPORT, 0);
        switch (code) {
            case CODE_EXTRA_GET_AIRPORT:
                flightAirportPickerBackgroundPresenter.getAirportListCloud(versionAirport);
                break;
        }
    }

    public static void startService(Context context, long versionCloudAirport) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, GetAirportListService.class);
        intent.putExtra(ARG_EXTRA_GET_AIRPORT, CODE_EXTRA_GET_AIRPORT);
        intent.putExtra(ARG_EXTRA_VERSION_AIRPORT, versionCloudAirport);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        DaggerFlightAirportComponent
                .builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
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

    @Override
    public void onDestroy() {
        flightAirportPickerBackgroundPresenter.detachView();
        super.onDestroy();
    }
}
