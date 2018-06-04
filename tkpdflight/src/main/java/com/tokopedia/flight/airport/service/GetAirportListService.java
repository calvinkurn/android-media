package com.tokopedia.flight.airport.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.TkpdFlight;
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportModule;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public class GetAirportListService extends JobIntentService implements FlightAirportPickerBackgroundContract.View {
    private static final int JOB_ID = 1001;
    private static final String ARG_EXTRA_GET_AIRPORT = "ARG_EXTRA_GET_AIRPORT";
    private static final int CODE_EXTRA_GET_AIRPORT = 432;
    private static final String ARG_EXTRA_VERSION_AIRPORT = "ARG_EXTRA_VERSION_AIRPORT";

    public GetAirportListService() {

    }

    @Inject
    FlightAirportPickerBackgroundPresenter flightAirportPickerBackgroundPresenter;

    public static void startService(Context context, long versionCloudAirport) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, GetAirportListService.class);
        intent.putExtra(ARG_EXTRA_GET_AIRPORT, CODE_EXTRA_GET_AIRPORT);
        intent.putExtra(ARG_EXTRA_VERSION_AIRPORT, versionCloudAirport);
        GetAirportListService.enqueueWork(context, intent);
    }

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, GetAirportListService.class, JOB_ID, work);
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

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        int code = intent.getIntExtra(ARG_EXTRA_GET_AIRPORT, 0);
        long versionAirport = intent.getLongExtra(ARG_EXTRA_VERSION_AIRPORT, 0);
        switch (code) {
            case CODE_EXTRA_GET_AIRPORT:
                flightAirportPickerBackgroundPresenter.getAirportListCloud(versionAirport);
                break;
        }
    }
}
