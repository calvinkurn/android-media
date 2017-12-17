package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.view.presenter.FlightBaseBookingContact;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public interface FlightBookingReviewContract {

    interface View extends FlightBaseBookingContact.View{

        void onErrorCheckVoucherCode(Throwable t);

        void onSuccessCheckVoucherCode(AttributesVoucher attributesVoucher);

        void onErrorSubmitData(Throwable e);

        void onSuccessSubmitData();

        void hideProgressDialog();

        void showProgressDialog();

        void setTimeStamp(String timestamp);

        void setTotalPrice(int totalPrice);

        BaseCartData getCurrentCartData();

        FlightBookingReviewModel getCurrentBookingReviewModel();

        String getDepartureTripId();

        String getReturnTripId();

        String getIdEmpotencyKey(String s);

        boolean isRoundTrip();

        void onErrorVerifyCode(Throwable e);
    }

    interface Presenter extends FlightBaseBookingContact.Presenter<View>{

        void verifyBooking(String promoCode, int price, int adult, String cartId,
                           List<FlightBookingPassengerViewModel> flightPassengerViewModels,
                           String contactName, String country, String email, String phone);

        void checkVoucherCode(String cartId, String voucherCode);

        void submitData();
    }
}
