package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.data.cloud.entity.Amenity;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created by alvarisi on 11/8/17.
 */

public interface FlightBookingContract {
    interface View extends CustomerView {

        String getContactName();

        void showContactNameEmptyError(@StringRes int resId);

        String getContactEmail();

        void showContactEmailEmptyError(@StringRes int resId);

        void showContactEmailInvalidError(@StringRes int resId);

        String getContactPhoneNumber();

        void showContactPhoneNumberEmptyError(@StringRes int resId);

        FlightBookingParamViewModel getCurrentBookingParamViewModel();

        void showAndRenderReturnTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightSearchViewModel returnTrip);

        void showAndRenderDepartureTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightSearchViewModel departureTrip);

        String getString(@StringRes int resId);

        void renderPassengersList(List<FlightBookingPassengerViewModel> passengerViewModels);

        void renderPhoneCodeView(String countryPhoneCode);

        String getDepartureTripId();

        String getReturnTripId();

        void navigateToDetailTrip(FlightSearchViewModel departureTrip);

        String getIdEmpotencyKey(String departureTripId);

        void renderLuggageList(List<Amenity> amenities);

        void showFullPageLoading();

        void hideFullPageLoading();

        void setCartData(FlightBookingCartData flightBookingCartData);

        FlightBookingCartData getCurrentCartPassData();

        void getRenderPriceDetails(List<SimpleViewModel> prices);

        void renderTotalPrices(String totalPrice);

        void showGetCartDataErrorStateLayout();

        void renderFinishTimeCountDown(Date date);

        void showExpireTransactionDialog();

        void showPriceDialogChanges(String newTotalPrice, String oldTotalPrice);
    }

    interface Presenter extends CustomerPresenter<View> {

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

    }
}
