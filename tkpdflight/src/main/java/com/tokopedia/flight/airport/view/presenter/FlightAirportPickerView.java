package com.tokopedia.flight.airport.view.presenter;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.airport.view.viewmodel.FlightCountryAirportViewModel;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightAirportPickerView extends BaseListViewListener<FlightCountryAirportViewModel> {

    void showGetAirportListLoading();

    void hideGetAirportListLoading();

    Activity getActivity();
}
