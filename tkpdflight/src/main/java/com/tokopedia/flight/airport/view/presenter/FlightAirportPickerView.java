package com.tokopedia.flight.airport.view.presenter;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightAirportPickerView extends BaseListViewListener<FlightAirportDB> {
    void updateAirportListOnBackground();

    void showGetAirportListLoading();

    void hideGetAirportListLoading();

    Activity getActivity();
}
