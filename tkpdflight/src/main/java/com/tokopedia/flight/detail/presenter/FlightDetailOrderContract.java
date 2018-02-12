package com.tokopedia.flight.detail.presenter;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
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

        void updateOrderData(String transactionDate, String eTicketLink, String invoiceLink, String cancelUrl);

        String getString(int id, Object... args);

        void updateViewStatus(String orderStatusString, int color, boolean isTicketVisible, boolean isScheduleVisible,
                              boolean isCancelVisible, boolean isReorderVisible);

        Activity getActivity();

        String getCancelMessage();

        void navigateToWebview(String url);

        void navigateToFlightHomePage();

        void renderFlightOrder(FlightOrder flightOrder);

        FlightOrder getFlightOrder();

        void navigateToContactUs(FlightOrder flightOrder);

        void showPaymentInfoLayout();

        void hidePaymentInfoLayout();

        void setPaymentLabel(@StringRes int resId);

        void setPaymentDescription(CharSequence description);

        void setTotalTransfer(String price);

        void hideTotalTransfer();

        void setPaymentDueDate(String dueDate);

        void hidePaymentDueDate();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getDetail(String orderId, FlightOrderDetailPassData flightOrderDetailPassData);

        void actionCancelOrderButtonClicked();

        void onHelpButtonClicked();

        void actionReorderButtonClicked();
    }
}
