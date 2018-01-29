package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created by alvarisi on 11/29/17.
 */

public interface FlightBaseBookingContact {
    interface View extends CustomerView {
        void showPriceChangesDialog(String newTotalPrice, String oldTotalPrice);

        void hideUpdatePriceLoading(); /*showUpdateDataLoading*/

        String getString(int flightbooking_price_adult_label);

        void showUpdatePriceLoading();

        FlightDetailViewModel getDepartureFlightDetailViewModel();

        FlightDetailViewModel getReturnFlightDetailViewModel();

        List<FlightBookingPassengerViewModel> getFlightBookingPassengers();

        void renderPriceListDetails(List<SimpleViewModel> simpleViewModels);

        void renderFinishTimeCountDown(Date date);

        void showUpdateDataErrorStateLayout(Throwable t);

        void showExpireTransactionDialog();

        void setCartId(String id);
    }

    interface Presenter<T extends View> extends CustomerPresenter<T> {
        void onUpdateCart();
    }
}
