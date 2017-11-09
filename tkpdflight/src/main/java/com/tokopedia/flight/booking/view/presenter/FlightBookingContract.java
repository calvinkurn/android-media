package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

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

        FlightBookingParamViewModel getCurrentBookingParam();

        void renderPhoneCodeView(String countryPhoneCode);

        String getDepartureTripId();

        String getReturnTripId();

        void navigateToDetailTrip(FlightSearchViewModel departureTrip);
    }

    interface Presenter extends CustomerPresenter<View> {

        void onButtonSubmitClicked();

        void initialize();

        void onPhoneCodeResultReceived(FlightBookingPhoneCodeViewModel phoneCodeViewModel);

        void onPassengerResultReceived(FlightBookingPassengerViewModel passengerViewModel);

        void onDepartureInfoClicked();

        void onReturnInfoClicked();

    }
}
