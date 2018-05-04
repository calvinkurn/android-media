package com.tokopedia.flight.cancellation.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;

/**
 * @author by furqan on 03/05/18.
 */

public interface FlightCancellationDetailContract {
    interface View extends CustomerView {

        void removePassengerRecyclerDivider();

        void addPassengerRecyclerDivider();

        FlightCancellationListViewModel getFlightCancellationList();

    }

    interface Presenter {

        void checkPassengersSize();

        void onViewCreated();

    }
}
