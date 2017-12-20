package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created by alvarisi on 11/8/17.
 */

public interface FlightBookingContract {
    interface View extends FlightBaseBookingContact.View {

        String getContactName();

        void showContactNameEmptyError(@StringRes int resId);

        void showContactNameInvalidError(@StringRes int resId);

        String getContactEmail();

        void showContactEmailEmptyError(@StringRes int resId);

        void showContactEmailInvalidError(@StringRes int resId);

        String getContactPhoneNumber();

        void showContactPhoneNumberEmptyError(@StringRes int resId);

        FlightBookingParamViewModel getCurrentBookingParamViewModel();

        void showAndRenderReturnTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightDetailViewModel returnTrip);

        void showAndRenderDepartureTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightDetailViewModel departureTrip);

        String getString(@StringRes int resId);

        void renderPassengersList(List<FlightBookingPassengerViewModel> passengerViewModels);

        void renderPhoneCodeView(String countryPhoneCode);

        String getDepartureTripId();

        String getReturnTripId();

        void navigateToDetailTrip(FlightDetailViewModel departureTrip);

        String getIdEmpotencyKey(String departureTripId);

        void showFullPageLoading();

        void hideFullPageLoading();

        void setCartData(FlightBookingCartData flightBookingCartData);

        FlightBookingCartData getCurrentCartPassData();

        void renderPriceListDetails(List<SimpleViewModel> prices);

        void renderTotalPrices(String totalPrice);

        void showGetCartDataErrorStateLayout(String errorMessage);

        void renderFinishTimeCountDown(Date date);

        void showExpireTransactionDialog();

        void showPriceChangesDialog(String newTotalPrice, String oldTotalPrice);

        void navigateToReview(FlightBookingReviewModel flightBookingReviewModel);

        void showUpdatePriceLoading();

        void hideUpdatePriceLoading();

        void showUpdateDataErrorStateLayout(String errorMessage);

        void showPassengerInfoNotFullfilled(@StringRes int resId);

        void navigateToPassengerInfoDetail(FlightBookingPassengerViewModel viewModel, boolean isAirAsiaAirline);
    }

    interface Presenter extends FlightBaseBookingContact.Presenter<View> {

        void onButtonSubmitClicked();

        void onPhoneCodeResultReceived(FlightBookingPhoneCodeViewModel phoneCodeViewModel);

        void onPassengerResultReceived(FlightBookingPassengerViewModel passengerViewModel);

        void onDepartureInfoClicked();

        void onReturnInfoClicked();

        void processGetCartData();

        void onResume();

        void onRetryGetCartData();

        void onDestroyView();

        void onFinishTransactionTimeReached();

        void onPause();

        void onChangePassengerButtonClicked(FlightBookingPassengerViewModel viewModel, FlightBookingCartData cartData);
    }
}
