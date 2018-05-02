package com.tokopedia.flight.cancellation.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;

import java.util.List;

/**
 * @author by furqan on 30/04/18.
 */

public interface FlightCancellationListContract {

    interface View extends CustomerView {

        void renderList();

        List<FlightCancellationListViewModel> getFlightCancellationList();

        void setFlightCancellationList(List<FlightCancellationListViewModel> cancellationList);

    }

    interface Presenter {
        void onViewCreated();
    }

}
