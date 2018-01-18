package com.tokopedia.flight.dashboard.view.fragment.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightDashboardCache {
    private static final String CACHE_NAME = "FlightDashboardCache";
    private static final String DEPARTURE = "DEPARTURE";
    private static final String ARRIVAL = "ARRIVAL";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefs;
    private Gson gson;

    public FlightDashboardCache(Context context, Gson gson) {
        this.sharedPrefs = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
        this.gson = gson;
    }

    public void putDepartureAirport(FlightAirportDB airportDB) {
        editor
                .putString(DEPARTURE, gson.toJson(airportDB))
                .apply();
    }

    public FlightAirportDB getDepartureAirport() {
        return gson.fromJson(sharedPrefs.getString(DEPARTURE, ""), FlightAirportDB.class);
    }

    public void putArrivalAirport(FlightAirportDB airportDB) {
        editor
                .putString(ARRIVAL, gson.toJson(airportDB))
                .apply();
    }

    public FlightAirportDB getArrivalAirport() {
        return gson.fromJson(sharedPrefs.getString(ARRIVAL, ""), FlightAirportDB.class);
    }
}
