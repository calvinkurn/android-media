package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;

import javax.inject.Inject;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationPresenter extends BaseDaggerPresenter<FlightCancellationContract.View>
        implements FlightCancellationContract.Presenter{



    @Inject
    public FlightCancellationPresenter() {
    }
}
