package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import javax.inject.Inject;

/**
 * Created by alvarisi on 11/8/17.
 */

public class FlightBookingPresenter extends BaseDaggerPresenter<FlightBookingContract.View> implements FlightBookingContract.Presenter {

    @Inject
    public FlightBookingPresenter() {
    }

    @Override
    public void onButtonSubmitClicked() {

    }
}
