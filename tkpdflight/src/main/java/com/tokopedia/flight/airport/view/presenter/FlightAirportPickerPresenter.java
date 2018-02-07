package com.tokopedia.flight.airport.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightAirportPickerPresenter extends CustomerPresenter<FlightAirportPickerView> {
    void getAirportList(String text, boolean isFirstTime);

    void checkAirportVersion(long currentVersion);
}
