package com.tokopedia.flight.airport.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightAirportPickerPresenter extends CustomerPresenter<FlightAirportPickerView> {
    void getAirportList(String text);

    void checkAirportVersion(long currentVersion);
}
