package com.tokopedia.flight.detail.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public interface FlightDetailOrderContract {
    interface View extends CustomerView{
        void showProgressDialog();

        void hideProgressDialog();

        void onErrorGetOrderDetail(Throwable e);

        void updateFlightList(List<FlightOrderJourney> journeys);

        void updatePassengerList(List<FlightDetailPassenger> flightDetailPassengers);

        void updatePrice(List<SimpleViewModel> priceList, String totalPrice);

        void updateOrderData(String transactionDate, String eTicketLink, String invoiceLink);

        String getString(int id, Object... args);

        void updateViewExpired();

        void updateViewConfirmed();

        void updateViewFailed();

        void updateViewFinished();

        void updateViewProgress();

        void updateViewReadyForQueue();

        void updateViewRefunded();

        void updateViewWaitingForPayment();

        void updateViewWaitingForThirdParty();

        void updateViewWaitingForTransfer();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getDetail(String orderId, FlightOrderDetailPassData flightOrderDetailPassData);
    }
}
