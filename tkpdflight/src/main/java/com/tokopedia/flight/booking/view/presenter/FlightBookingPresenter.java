package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.search.data.cloud.model.response.Fare;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by alvarisi on 11/8/17.
 */

public class FlightBookingPresenter extends BaseDaggerPresenter<FlightBookingContract.View> implements FlightBookingContract.Presenter {

    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;
    private FlightAddToCartUseCase flightAddToCartUseCase;
    private FlightBookingCartDataMapper flightBookingCartDataMapper;
    @NonNull
    private final BehaviorSubject<Boolean> loadingVisibility;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightBookingPresenter(FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase,
                                  FlightAddToCartUseCase flightAddToCartUseCase,
                                  FlightBookingCartDataMapper flightBookingCartDataMapper) {

        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
        this.flightAddToCartUseCase = flightAddToCartUseCase;
        this.flightBookingCartDataMapper = flightBookingCartDataMapper;
        this.loadingVisibility = BehaviorSubject.create(false);
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onButtonSubmitClicked() {
        if (validateFields()) {

        }
    }

    @Override
    public void initialize() {

    }

    private void renderUi(FlightBookingCartData flightBookingCartData) {
        int totalPrice = 0;
        getView().renderCartData(flightBookingCartData);
        totalPrice = flightBookingCartData.getDepartureTrip().getTotalNumeric();
        Fare fare = flightBookingCartData.getDepartureTrip().getFare();
        List<SimpleViewModel> simpleViewModels = new ArrayList<>();
        if (fare.getAdult() != null) {
            simpleViewModels.add(new SimpleViewModel(
                    String.format("%s - %s  1x %s",
                            flightBookingCartData.getDepartureTrip().getDepartureAirport(),
                            flightBookingCartData.getDepartureTrip().getArrivalAirport(),
                            getView().getString(R.string.flightbooking_price_adult_label)
                    ),
                    fare.getAdult())
            );
        }
        if (fare.getChild() != null) {
            simpleViewModels.add(new SimpleViewModel(
                    String.format("%s - %s 1x %s",
                            flightBookingCartData.getDepartureTrip().getDepartureAirport(),
                            flightBookingCartData.getDepartureTrip().getArrivalAirport(),
                            getView().getString(R.string.flightbooking_price_child_label)
                    ),
                    fare.getChild())
            );
        }
        if (fare.getInfant() != null) {
            simpleViewModels.add(new SimpleViewModel(
                    String.format("%s - %s 1x %s",
                            flightBookingCartData.getDepartureTrip().getDepartureAirport(),
                            flightBookingCartData.getDepartureTrip().getArrivalAirport(),
                            getView().getString(R.string.flightbooking_price_infant_label)
                    ),
                    fare.getInfant())
            );
        }
        getView().getRenderDeparturePrice(simpleViewModels);
        getView().showAndRenderDepartureTripCardDetail(getView().getCurrentBookingParamViewModel().getSearchParam(), flightBookingCartData.getDepartureTrip());
        if (isRoundTrip()) {
            totalPrice += flightBookingCartData.getReturnTrip().getTotalNumeric();
            Fare returnFare = flightBookingCartData.getDepartureTrip().getFare();
            List<SimpleViewModel> returnSimpleViewModels = new ArrayList<>();
            if (returnFare.getAdult() != null) {
                returnSimpleViewModels.add(new SimpleViewModel(
                        String.format("%s - %s  1x %s",
                                flightBookingCartData.getReturnTrip().getDepartureAirport(),
                                flightBookingCartData.getReturnTrip().getArrivalAirport(),
                                getView().getString(R.string.flightbooking_price_adult_label)
                        ),
                        returnFare.getAdult())
                );
            }
            if (returnFare.getChild() != null) {
                returnSimpleViewModels.add(new SimpleViewModel(
                        String.format("%s - %s 1x %s",
                                flightBookingCartData.getReturnTrip().getDepartureAirport(),
                                flightBookingCartData.getReturnTrip().getArrivalAirport(),
                                getView().getString(R.string.flightbooking_price_child_label)
                        ),
                        returnFare.getChild())
                );
            }
            if (returnFare.getInfant() != null) {
                returnSimpleViewModels.add(new SimpleViewModel(
                        String.format("%s - %s 1x %s",
                                flightBookingCartData.getReturnTrip().getDepartureAirport(),
                                flightBookingCartData.getReturnTrip().getArrivalAirport(),
                                getView().getString(R.string.flightbooking_price_infant_label)
                        ),
                        returnFare.getInfant())
                );
            }
            getView().getRenderReturnPrice(returnSimpleViewModels);
            getView().showAndRenderReturnTripCardDetail(getView().getCurrentBookingParamViewModel().getSearchParam(), flightBookingCartData.getReturnTrip());
        }

        List<FlightBookingPassengerViewModel> passengerViewModels = buildPassengerViewModel(getView().getCurrentBookingParamViewModel().getSearchParam());
        getView().getCurrentBookingParamViewModel().setPassengerViewModels(passengerViewModels);
        getView().renderPassengersList(passengerViewModels);

        getView().renderTotalPrices(String.valueOf(totalPrice));
    }

    @Override
    public void onPhoneCodeResultReceived(FlightBookingPhoneCodeViewModel phoneCodeViewModel) {
        getView().getCurrentBookingParamViewModel().setPhoneCodeViewModel(phoneCodeViewModel);
        getView().renderPhoneCodeView(phoneCodeViewModel.getCountryPhoneCode());
    }

    @Override
    public void onPassengerResultReceived(FlightBookingPassengerViewModel passengerViewModel) {
        List<FlightBookingPassengerViewModel> passengerViewModels = getView().getCurrentBookingParamViewModel().getPassengerViewModels();
        int indexPassenger = passengerViewModels.indexOf(passengerViewModel);
        if (indexPassenger != -1) {
            passengerViewModels.set(indexPassenger, passengerViewModel);
        }
        getView().renderPassengersList(passengerViewModels);
    }

    @Override
    public void onDepartureInfoClicked() {
        getView().navigateToDetailTrip(getView().getCurrentCartPassData().getDepartureTrip());
    }

    @Override
    public void onReturnInfoClicked() {
        getView().navigateToDetailTrip(getView().getCurrentCartPassData().getReturnTrip());
    }

    @Override
    public void processGetCartData() {
        RequestParams requestParams;
        if (isRoundTrip()) {
            requestParams = flightAddToCartUseCase.createRequestParam(
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getAdult(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getChildren(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getInfant(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getDepartureDate(),
                    getView().getReturnTripId(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getReturnDate(),
                    getView().getIdEmpotencyKey(getView().getDepartureTripId() + "_" + getView().getReturnTripId())
            );
        } else {
            requestParams = flightAddToCartUseCase.createRequestParam(
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getAdult(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getChildren(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getInfant(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getDepartureDate(),
                    getView().getIdEmpotencyKey(getView().getDepartureTripId())
            );
        }
        compositeSubscription.add(flightAddToCartUseCase.createObservable(requestParams)
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadingVisibility.onNext(false);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        loadingVisibility.onNext(true);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        loadingVisibility.onNext(false);
                    }
                })
                .map(new Func1<CartEntity, FlightBookingCartData>() {
                    @Override
                    public FlightBookingCartData call(CartEntity entity) {
                        return flightBookingCartDataMapper.transform(entity);
                    }
                })
                .zipWith(flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, getView().getDepartureTripId())), new Func2<FlightBookingCartData, FlightSearchViewModel, FlightBookingCartData>() {
                    @Override
                    public FlightBookingCartData call(FlightBookingCartData flightBookingCartData, FlightSearchViewModel flightSearchViewModel) {
                        flightBookingCartData.setDepartureTrip(flightSearchViewModel);
                        return flightBookingCartData;
                    }
                })
                .flatMap(new Func1<FlightBookingCartData, Observable<FlightBookingCartData>>() {
                    @Override
                    public Observable<FlightBookingCartData> call(FlightBookingCartData flightBookingCartData) {
                        if (isRoundTrip()) {
                            return Observable.just(flightBookingCartData)
                                    .zipWith(flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(true, getView().getReturnTripId())),
                                            new Func2<FlightBookingCartData, FlightSearchViewModel, FlightBookingCartData>() {
                                                @Override
                                                public FlightBookingCartData call(FlightBookingCartData flightBookingCartData, FlightSearchViewModel flightSearchViewModel) {
                                                    flightBookingCartData.setReturnTrip(flightSearchViewModel);
                                                    return flightBookingCartData;
                                                }
                                            });
                        }
                        return Observable.just(flightBookingCartData);
                    }
                })
                .onBackpressureDrop()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FlightBookingCartData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(FlightBookingCartData flightBookingCartData) {
                        if (isViewAttached()) {
                            renderUi(flightBookingCartData);
                        }
                    }
                })
        );

    }

    @Override
    public void onResume() {
        compositeSubscription.add(getLoadingIndicatorVisibility()
                .onBackpressureDrop()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (isViewAttached())
                            if (aBoolean) {
                                getView().showFullPageLoading();
                            } else {
                                getView().hideFullPageLoading();
                            }
                    }
                }));
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

    private Observable<Boolean> isRoundTripObservable() {
        return Observable.just(isRoundTrip());
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

    private Observable<Boolean> getLoadingIndicatorVisibility() {
        return loadingVisibility.asObservable();
    }
}
