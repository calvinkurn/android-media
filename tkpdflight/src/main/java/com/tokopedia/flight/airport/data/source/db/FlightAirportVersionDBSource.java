package com.tokopedia.flight.airport.data.source.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.R;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 12/8/17.
 */

public class FlightAirportVersionDBSource  {

    private static final String PREF_NAME = "FLIGHT";
    public static final String FLIGHT_VERSION_AIRPORT = "FLIGHT_VERSION_AIRPORT";
    private Context context;
    private SharedPreferences sharedPreferences;

    @Inject
    public FlightAirportVersionDBSource(@ApplicationContext Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setVersionDefault() {
        sharedPreferences.edit().putLong(FLIGHT_VERSION_AIRPORT, context.getResources().getInteger(R.integer.version_aiport)).apply();
    }

    public long getVersion() {
        return sharedPreferences.getLong(FLIGHT_VERSION_AIRPORT, 0);
    }

    public void updateVersion(long versionAirport) {
        sharedPreferences.edit().putLong(FLIGHT_VERSION_AIRPORT, versionAirport).apply();
    }
}
