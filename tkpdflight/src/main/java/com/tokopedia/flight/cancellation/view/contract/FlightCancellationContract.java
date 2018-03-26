package com.tokopedia.flight.cancellation.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

import java.util.List;

/**
 * @author by furqan on 21/03/18.
 */

public interface FlightCancellationContract {

    interface View extends CustomerView {

        void renderCancelableList();

        void setFlightCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList);

        String getInvoiceId();

        String getString(int resId);

        List<FlightCancellationJourney> getFlightCancellationJourney();

        List<FlightCancellationViewModel> getCurrentFlightCancellationViewModel();

    }

    interface Presenter {

        void onViewCreated();

    }
}
