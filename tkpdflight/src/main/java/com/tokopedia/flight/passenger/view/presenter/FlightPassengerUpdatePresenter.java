package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import javax.inject.Inject;

/**
 * @author by furqan on 12/03/18.
 */

public class FlightPassengerUpdatePresenter extends BaseDaggerPresenter<FlightPassengerUpdateContract.View>
        implements FlightPassengerUpdateContract.Presenter{

    @Inject
    public FlightPassengerUpdatePresenter() {
    }
}
