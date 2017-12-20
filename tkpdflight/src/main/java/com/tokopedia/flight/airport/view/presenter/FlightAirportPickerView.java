package com.tokopedia.flight.airport.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener2;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightAirportPickerView extends BaseListViewListener2<FlightAirportDB> {
    void updateAirportListOnBackground();

    void showGetAirportListLoading();

    void hideGetAirportListLoading();
}
