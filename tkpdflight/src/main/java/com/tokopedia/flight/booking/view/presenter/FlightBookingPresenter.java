package com.tokopedia.flight.booking.view.presenter;

import android.util.Patterns;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by alvarisi on 11/8/17.
 */

public class FlightBookingPresenter extends BaseDaggerPresenter<FlightBookingContract.View> implements FlightBookingContract.Presenter {

    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;

    @Inject
    public FlightBookingPresenter(FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase) {

        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
    }

    @Override
    public void onButtonSubmitClicked() {
        if (validateFields()) {

        }
    }

    @Override
    public void initialize() {
        if (isRoundTrip()) {
            Observable.zip(
                    flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, getView().getDepartureTripId())),
                    flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(true, getView().getReturnTripId())),
                    new Func2<FlightSearchViewModel, FlightSearchViewModel, FlightBookingParamViewModel>() {
                        @Override
                        public FlightBookingParamViewModel call(FlightSearchViewModel departureFlight, FlightSearchViewModel returnFlight) {
                            FlightBookingParamViewModel flightBookingTripViewModel = getView().getCurrentBookingParamViewModel();
                            flightBookingTripViewModel.setDepartureTrip(departureFlight);
                            flightBookingTripViewModel.setReturnTrip(returnFlight);
                            return flightBookingTripViewModel;
                        }
                    }).onBackpressureDrop()
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<FlightBookingParamViewModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(FlightBookingParamViewModel flightBookingParamViewModel) {
                            if (isViewAttached()) {
                                renderUi();
                            }
                        }
                    });
        } else {
            flightBookingGetSingleResultUseCase.execute(flightBookingGetSingleResultUseCase.createRequestParam(false, getView().getDepartureTripId()), new Subscriber<FlightSearchViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();

                }

                @Override
                public void onNext(FlightSearchViewModel departureFlight) {
                    if (isViewAttached()) {
                        FlightBookingParamViewModel flightBookingTripViewModel = getView().getCurrentBookingParamViewModel();
                        flightBookingTripViewModel.setDepartureTrip(departureFlight);
                        renderUi();
                    }
                }
            });
        }


    }

    private void renderUi() {
        if (isRoundTrip()) {
            getView().showAndRenderDepartureTripCardDetail(getView().getCurrentBookingParamViewModel().getSearchParam(), getView().getCurrentBookingParamViewModel().getDepartureTrip());
            getView().showAndRenderReturnTripCardDetail(getView().getCurrentBookingParamViewModel().getSearchParam(), getView().getCurrentBookingParamViewModel().getReturnTrip());
        } else {
            getView().showAndRenderDepartureTripCardDetail(getView().getCurrentBookingParamViewModel().getSearchParam(), getView().getCurrentBookingParamViewModel().getDepartureTrip());
        }

        List<FlightBookingPassengerViewModel> passengerViewModels = buildPassengerViewModel(getView().getCurrentBookingParamViewModel().getSearchParam());
        getView().getCurrentBookingParam().setPassengerViewModels(passengerViewModels);
        getView().renderPassengersList(passengerViewModels);
        // TODO : Calculate and render "Rincian Harga"
    }

    @Override
    public void onPhoneCodeResultReceived(FlightBookingPhoneCodeViewModel phoneCodeViewModel) {
        getView().getCurrentBookingParam().setPhoneCodeViewModel(phoneCodeViewModel);
        getView().renderPhoneCodeView(phoneCodeViewModel.getCountryPhoneCode());
    }

    @Override
    public void onPassengerResultReceived(FlightBookingPassengerViewModel passengerViewModel) {
        List<FlightBookingPassengerViewModel> passengerViewModels = getView().getCurrentBookingParam().getPassengerViewModels();
        int indexPassenger = passengerViewModels.indexOf(passengerViewModel);
        if (indexPassenger != -1) {
            passengerViewModels.set(indexPassenger, passengerViewModel);
        }
        getView().renderPassengersList(passengerViewModels);
    }

    @Override
    public void onDepartureInfoClicked() {
        getView().navigateToDetailTrip(getView().getCurrentBookingParam().getDepartureTrip());
    }

    @Override
    public void onReturnInfoClicked() {
        getView().navigateToDetailTrip(getView().getCurrentBookingParam().getReturnTrip());
    }

    private List<FlightBookingPassengerViewModel> buildPassengerViewModel(FlightSearchPassDataViewModel passData) {
        boolean isSingleRoute = !isRoundTrip();
        int passengerNumber = 1;
        List<FlightBookingPassengerViewModel> viewModels = new ArrayList<>();
        for (int i = 1, adultTotal = passData.getFlightPassengerViewModel().getAdult(); i <= adultTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setPassengerId(passengerNumber);
            viewModel.setType(FlightBookingPassenger.ADULT);
            viewModel.setSingleRoute(isSingleRoute);
            viewModel.setHeaderTitle(String.format("%s %d (%s)",
                    getView().getString(R.string.flight_booking_prefix_passenger),
                    passengerNumber,
                    getView().getString(R.string.flight_booking_postfix_adult_passenger))
            );
            viewModels.add(viewModel);
            passengerNumber++;
        }
        for (int i = 1, childTotal = passData.getFlightPassengerViewModel().getChildren(); i <= childTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setPassengerId(passengerNumber);
            viewModel.setType(FlightBookingPassenger.CHILDREN);
            viewModel.setSingleRoute(isSingleRoute);
            viewModel.setHeaderTitle(String.format("%s %d (%s)",
                    getView().getString(R.string.flight_booking_prefix_passenger),
                    passengerNumber,
                    getView().getString(R.string.flight_booking_postfix_children_passenger))
            );
            viewModels.add(viewModel);
            passengerNumber++;
        }
        for (int i = 1, infantTotal = passData.getFlightPassengerViewModel().getChildren(); i <= infantTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setPassengerId(passengerNumber);
            viewModel.setType(FlightBookingPassenger.INFANT);
            viewModel.setSingleRoute(isSingleRoute);
            viewModel.setHeaderTitle(String.format("%s %d (%s)",
                    getView().getString(R.string.flight_booking_prefix_passenger),
                    passengerNumber,
                    getView().getString(R.string.flight_booking_postfix_infant_passenger))
            );
            viewModels.add(viewModel);
            passengerNumber++;
        }
        return viewModels;
    }

    private boolean isRoundTrip() {
        return getView().getReturnTripId() != null && getView().getReturnTripId().length() > 0;
    }

    private boolean validateFields() {
        boolean isValid = true;
        if (getView().getContactName().length() == 0) {
            isValid = false;
            getView().showContactNameEmptyError(R.string.flight_booking_contact_name_empty_error);
        } else if (getView().getContactEmail().length() == 0) {
            isValid = false;
            getView().showContactEmailEmptyError(R.string.flight_booking_contact_email_empty_error);
        } else if (isValidEmail(getView().getContactEmail())) {
            isValid = false;
            getView().showContactEmailInvalidError(R.string.flight_booking_contact_email_invalid_error);
        } else if (getView().getContactPhoneNumber().length() == 0) {
            isValid = false;
            getView().showContactPhoneNumberEmptyError(R.string.flight_booking_contact_phone_empty_error);
        }
        return isValid;
    }

    private boolean isValidEmail(String contactEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches();
    }
}
