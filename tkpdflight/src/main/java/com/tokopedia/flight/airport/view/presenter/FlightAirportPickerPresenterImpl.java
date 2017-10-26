package com.tokopedia.flight.airport.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportPickerPresenterImpl extends BaseDaggerPresenter<FlightAirportPickerView> implements FlightAirportPickerPresenter {

    @Inject
    public FlightAirportPickerPresenterImpl() {
    }
}
