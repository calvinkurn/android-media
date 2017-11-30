package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.domain.FlightBookingGetPhoneCodeUseCase;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.search.data.cloud.model.response.Fare;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by alvarisi on 11/8/17.
 */

public class FlightBookingPresenter extends FlightBaseBookingPresenter<FlightBookingContract.View>
        implements FlightBookingContract.Presenter {

    private static final String DEFAULT_COUNTRY_CODE_PHONE_NUMBER = "ID";
    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;
    private FlightAddToCartUseCase flightAddToCartUseCase;
    private FlightBookingCartDataMapper flightBookingCartDataMapper;
    private FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightBookingPresenter(FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase,
                                  FlightAddToCartUseCase flightAddToCartUseCase,
                                  FlightBookingCartDataMapper flightBookingCartDataMapper,
                                  FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase) {
        super(flightAddToCartUseCase, flightBookingCartDataMapper);
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
        this.flightAddToCartUseCase = flightAddToCartUseCase;
        this.flightBookingCartDataMapper = flightBookingCartDataMapper;
        this.flightBookingGetPhoneCodeUseCase = flightBookingGetPhoneCodeUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onButtonSubmitClicked() {
        if (validateFields()) {
            getView().getCurrentBookingParamViewModel().setContactName(getView().getContactName());
            getView().getCurrentBookingParamViewModel().setContactEmail(getView().getContactEmail());
            getView().getCurrentBookingParamViewModel().setContactPhone(getView().getContactPhoneNumber());
            FlightBookingReviewModel flightBookingReviewModel =
                    new FlightBookingReviewModel(getView().getCurrentBookingParamViewModel(),
                            getView().getCurrentCartPassData());
            getView().navigateToReview(flightBookingReviewModel);
        }
    }

    private void renderUi(FlightBookingCartData flightBookingCartData) {
        getView().setCartData(flightBookingCartData);
        if (flightBookingCartData.getNewFarePrices() != null && flightBookingCartData.getNewFarePrices().size() > 0) {
            int totalPrice = 0, oldTotalPrice = 0;
            for (NewFarePrice newFarePrice : flightBookingCartData.getNewFarePrices()) {
                totalPrice += newFarePrice.getFare().getAdultNumeric() +
                        newFarePrice.getFare().getChildNumeric() +
                        newFarePrice.getFare().getInfantNumeric();

                if (newFarePrice.getId().equalsIgnoreCase(flightBookingCartData.getDepartureTrip().getId())) {
                    oldTotalPrice += flightBookingCartData.getDepartureTrip().getTotalNumeric();
                    flightBookingCartData.getDepartureTrip().setAdultNumericPrice(newFarePrice.getFare().getAdultNumeric());
                    flightBookingCartData.getDepartureTrip().setChildNumericPrice(newFarePrice.getFare().getChildNumeric());
                    flightBookingCartData.getDepartureTrip().setInfantNumericPrice(newFarePrice.getFare().getInfantNumeric());
                } else if (isRoundTrip() && newFarePrice.getId().equalsIgnoreCase(flightBookingCartData.getReturnTrip().getId())) {
                    oldTotalPrice += flightBookingCartData.getReturnTrip().getTotalNumeric();
                    flightBookingCartData.getReturnTrip().setAdultNumericPrice(newFarePrice.getFare().getAdultNumeric());
                    flightBookingCartData.getReturnTrip().setChildNumericPrice(newFarePrice.getFare().getChildNumeric());
                    flightBookingCartData.getReturnTrip().setInfantNumericPrice(newFarePrice.getFare().getInfantNumeric());
                }
            }
            if (totalPrice != oldTotalPrice) {
                getView().showPriceChangesDialog(convertPriceValueToIdrFormat(totalPrice), convertPriceValueToIdrFormat(oldTotalPrice));
            }
            getView().getCurrentBookingParamViewModel().setTotalPriceNumeric(totalPrice);
            getView().getCurrentBookingParamViewModel().setTotalPriceFmt(convertPriceValueToIdrFormat(totalPrice));
        }

        getView().showAndRenderDepartureTripCardDetail(getView().getCurrentBookingParamViewModel().getSearchParam(),
                flightBookingCartData.getDepartureTrip());
        if (isRoundTrip()) {
            getView().showAndRenderReturnTripCardDetail(getView().getCurrentBookingParamViewModel().getSearchParam(),
                    flightBookingCartData.getReturnTrip());
        }
        if (getView().getCurrentBookingParamViewModel().getPassengerViewModels() == null) {
            List<FlightBookingPassengerViewModel> passengerViewModels = buildPassengerViewModel(getView().getCurrentBookingParamViewModel().getSearchParam());
            getView().getCurrentBookingParamViewModel().setPassengerViewModels(passengerViewModels);
            getView().renderPassengersList(passengerViewModels);
        }
        getView().getCurrentBookingParamViewModel().setPhoneCodeViewModel(flightBookingCartData.getDefaultPhoneCode());
        getView().renderPhoneCodeView(String.format("+%s", getView().getCurrentBookingParamViewModel().getPhoneCodeViewModel().getCountryPhoneCode()));
        Date expiredDate = FlightDateUtil.addTimeToCurrentDate(Calendar.SECOND, flightBookingCartData.getRefreshTime());
        getView().getCurrentBookingParamViewModel().setOrderDueTimestamp(FlightDateUtil.dateToString(expiredDate, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT));
        getView().renderFinishTimeCountDown(expiredDate);
        actionCalculatePriceAndRender(flightBookingCartData.getNewFarePrices(),
                flightBookingCartData.getDepartureTrip(),
                flightBookingCartData.getReturnTrip(),
                getView().getCurrentBookingParamViewModel().getPassengerViewModels()
        );

        BaseCartData baseCartData = getCurrentCartData();
        List<Fare> fares = new ArrayList<>();
        FlightDetailViewModel flightDetailViewModel = getView().getCurrentCartPassData().getDepartureTrip();
        fares.add(
                new Fare(
                        convertPriceValueToIdrFormat(flightDetailViewModel.getAdultNumericPrice()),
                        convertPriceValueToIdrFormat(flightDetailViewModel.getChildNumericPrice()),
                        convertPriceValueToIdrFormat(flightDetailViewModel.getInfantNumericPrice()),
                        flightDetailViewModel.getAdultNumericPrice(),
                        flightDetailViewModel.getChildNumericPrice(),
                        flightDetailViewModel.getInfantNumericPrice()
                )
        );

        int newTotalPrice = calculateTotalFareAndAmenities(
                fares,
                baseCartData.getAdult(),
                baseCartData.getChild(),
                baseCartData.getInfant(),
                baseCartData.getAmenities()
        );
        getView().renderTotalPrices(convertPriceValueToIdrFormat(newTotalPrice));
    }

    @Override
    public void onPhoneCodeResultReceived(FlightBookingPhoneCodeViewModel phoneCodeViewModel) {
        getView().getCurrentBookingParamViewModel().setPhoneCodeViewModel(phoneCodeViewModel);
        getView().renderPhoneCodeView(String.format("+%s", phoneCodeViewModel.getCountryPhoneCode()));
    }

    @Override
    public void onPassengerResultReceived(FlightBookingPassengerViewModel passengerViewModel) {
        List<FlightBookingPassengerViewModel> passengerViewModels = getView().getCurrentBookingParamViewModel().getPassengerViewModels();
        int indexPassenger = passengerViewModels.indexOf(passengerViewModel);
        if (indexPassenger != -1) {
            passengerViewModels.set(indexPassenger, passengerViewModel);
        }
        getView().renderPassengersList(passengerViewModels);

        actionCalculatePriceAndRender(
                getView().getCurrentCartPassData().getNewFarePrices(),
                getView().getCurrentCartPassData().getDepartureTrip(),
                getView().getCurrentCartPassData().getReturnTrip(),
                getView().getCurrentBookingParamViewModel().getPassengerViewModels()
        );
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
        getView().showFullPageLoading();
        compositeSubscription.add(flightAddToCartUseCase.createObservable(getRequestParams())
                .map(new Func1<CartEntity, FlightBookingCartData>() {
                    @Override
                    public FlightBookingCartData call(CartEntity entity) {
                        return flightBookingCartDataMapper.transform(entity);
                    }
                })
                .zipWith(getDepartureDataObservable(),
                        new Func2<FlightBookingCartData, FlightSearchViewModel, FlightBookingCartData>() {
                            @Override
                            public FlightBookingCartData call(FlightBookingCartData flightBookingCartData, FlightSearchViewModel flightSearchViewModel) {
                                FlightDetailViewModel flightDetailViewModel = new FlightDetailViewModel().build(flightSearchViewModel);
                                flightDetailViewModel.build(getView().getCurrentBookingParamViewModel().getSearchParam());
                                flightBookingCartData.setDepartureTrip(flightDetailViewModel);
                                return flightBookingCartData;
                            }
                        })
                .flatMap(getFlightRoundTripDataObservable())
                .zipWith(getDefaultPhoneDataObservable(),
                        new Func2<FlightBookingCartData, FlightBookingPhoneCodeViewModel, FlightBookingCartData>() {
                            @Override
                            public FlightBookingCartData call(FlightBookingCartData flightBookingCartData, FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
                                flightBookingCartData.setDefaultPhoneCode(flightBookingPhoneCodeViewModel);
                                return flightBookingCartData;
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
                        if (isViewAttached()) {
                            getView().hideFullPageLoading();
                            getView().showGetCartDataErrorStateLayout(FlightErrorUtil.getMessageFromException(e));
                        }
                    }

                    @Override
                    public void onNext(FlightBookingCartData flightBookingCartData) {
                        if (isViewAttached()) {
                            getView().hideFullPageLoading();
                            renderUi(flightBookingCartData);
                        }
                    }
                })
        );
    }

    private RequestParams getRequestParams() {
        RequestParams requestParams;
        if (isRoundTrip()) {
            requestParams = flightAddToCartUseCase.createRequestParam(
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getAdult(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getChildren(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getInfant(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getReturnTripId(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getDepartureDate(),
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
        return requestParams;
    }

    private Observable<FlightSearchViewModel> getDepartureDataObservable() {
        return flightBookingGetSingleResultUseCase
                .createObservable(flightBookingGetSingleResultUseCase
                        .createRequestParam(false, getView().getDepartureTripId()));
    }

    @NonNull
    private Observable<FlightBookingPhoneCodeViewModel> getDefaultPhoneDataObservable() {
        return flightBookingGetPhoneCodeUseCase.createObservable(RequestParams.EMPTY)
                .flatMap(new Func1<List<FlightBookingPhoneCodeViewModel>, Observable<FlightBookingPhoneCodeViewModel>>() {
                    @Override
                    public Observable<FlightBookingPhoneCodeViewModel> call(List<FlightBookingPhoneCodeViewModel> flightBookingPhoneCodeViewModels) {
                        return Observable.from(flightBookingPhoneCodeViewModels);
                    }
                })
                .filter(new Func1<FlightBookingPhoneCodeViewModel, Boolean>() {
                    @Override
                    public Boolean call(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
                        return flightBookingPhoneCodeViewModel.getCountryId().equalsIgnoreCase(DEFAULT_COUNTRY_CODE_PHONE_NUMBER);
                    }
                }).first();
    }

    @NonNull
    private Func1<FlightBookingCartData, Observable<FlightBookingCartData>> getFlightRoundTripDataObservable() {
        return new Func1<FlightBookingCartData, Observable<FlightBookingCartData>>() {
            @Override
            public Observable<FlightBookingCartData> call(FlightBookingCartData flightBookingCartData) {
                if (isRoundTrip()) {
                    return Observable.just(flightBookingCartData)
                            .zipWith(flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(true, getView().getReturnTripId())),
                                    new Func2<FlightBookingCartData, FlightSearchViewModel, FlightBookingCartData>() {
                                        @Override
                                        public FlightBookingCartData call(FlightBookingCartData flightBookingCartData, FlightSearchViewModel flightSearchViewModel) {
                                            FlightDetailViewModel flightDetailViewModel = new FlightDetailViewModel().build(flightSearchViewModel);
                                            flightDetailViewModel.build(getView().getCurrentBookingParamViewModel().getSearchParam());
                                            flightBookingCartData.setReturnTrip(flightDetailViewModel);
                                            return flightBookingCartData;
                                        }
                                    });
                }
                return Observable.just(flightBookingCartData);
            }
        };
    }

    @Override
    public void onResume() {

    }


    @Override
    public void onPause() {

    }

    @Override
    public void onRetryGetCartData() {
        processGetCartData();
    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        detachView();
    }

    @Override
    public void onFinishTransactionTimeReached() {
        if (isViewAttached()) {
            onUpdateCart();
            /*getView().showUpdateDataLoading();
            flightAddToCartUseCase.createObservable(getRequestParams())
                    .map(new Func1<CartEntity, FlightBookingCartData>() {
                        @Override
                        public FlightBookingCartData call(CartEntity entity) {
                            return flightBookingCartDataMapper.transform(entity);
                        }
                    }).map(new Func1<FlightBookingCartData, FlightBookingCartData>() {
                @Override
                public FlightBookingCartData call(FlightBookingCartData flightBookingCartData) {
                    FlightBookingCartData current = getView().getCurrentCartPassData();
                    current.setId(flightBookingCartData.getId());
                    current.setRefreshTime(flightBookingCartData.getRefreshTime());
                    current.setNewFarePrices(flightBookingCartData.getNewFarePrices());
                    current.setDefaultPhoneCode(getView().getCurrentBookingParamViewModel().getPhoneCodeViewModel());
                    return current;
                }
            }).onBackpressureDrop()
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<FlightBookingCartData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isViewAttached()) {
                                getView().hideUpdateDataLoading();
                                if (e instanceof FlightException &&
                                        ((FlightException) e).getErrorList().contains(new FlightError(FlightErrorConstant.ADD_TO_CART))) {
                                    getView().showExpireTransactionDialog();
                                } else {
                                    getView().showUpdateDataErrorStateLayout(FlightErrorUtil.getMessageFromException(e));
                                }
                            }
                        }

                        @Override
                        public void onNext(FlightBookingCartData flightBookingCartData) {
                            if (isViewAttached()) {
                                getView().hideUpdateDataLoading();
                                renderUi(flightBookingCartData);
                            }
                        }
                    });*/
        }
    }

    private String formatPassengerHeader(String prefix, int number, String postix) {
        return String.format(getView().getString(R.string.flight_booking_header_passenger_format),
                prefix,
                number,
                postix
        );
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
            viewModel.setHeaderTitle(
                    formatPassengerHeader(
                            getView().getString(R.string.flight_booking_prefix_passenger),
                            passengerNumber,
                            getView().getString(R.string.flight_booking_postfix_adult_passenger)
                    )
            );
            viewModel.setFlightBookingLuggageMetaViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
            viewModel.setFlightBookingMealMetaViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
            viewModels.add(viewModel);
            passengerNumber++;
        }
        for (int i = 1, childTotal = passData.getFlightPassengerViewModel().getChildren(); i <= childTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setPassengerId(passengerNumber);
            viewModel.setType(FlightBookingPassenger.CHILDREN);
            viewModel.setSingleRoute(isSingleRoute);
            viewModel.setHeaderTitle(
                    formatPassengerHeader(
                            getView().getString(R.string.flight_booking_prefix_passenger),
                            passengerNumber,
                            getView().getString(R.string.flight_booking_postfix_children_passenger))
            );
            viewModel.setFlightBookingMealMetaViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
            viewModel.setFlightBookingLuggageMetaViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
            viewModels.add(viewModel);
            passengerNumber++;

        }
        for (int i = 1, infantTotal = passData.getFlightPassengerViewModel().getInfant(); i <= infantTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setPassengerId(passengerNumber);
            viewModel.setType(FlightBookingPassenger.INFANT);
            viewModel.setSingleRoute(isSingleRoute);
            viewModel.setHeaderTitle(
                    formatPassengerHeader(
                            getView().getString(R.string.flight_booking_prefix_passenger),
                            passengerNumber,
                            getView().getString(R.string.flight_booking_postfix_infant_passenger))
            );

            viewModel.setFlightBookingLuggageMetaViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
            viewModel.setFlightBookingMealMetaViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
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
        } else if (!isValidEmail(getView().getContactEmail())) {
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

    @Override
    RequestParams getRequestParam() {
        return getRequestParams();
    }

    @Override
    BaseCartData getCurrentCartData() {
        BaseCartData baseCartData = new BaseCartData();
        baseCartData.setNewFarePrices(getView().getCurrentCartPassData().getNewFarePrices());
        List<FlightBookingAmenityViewModel> amenityViewModels = new ArrayList<>();
        for (FlightBookingPassengerViewModel passengerViewModel : getView().getCurrentBookingParamViewModel().getPassengerViewModels()) {
            for (FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel : passengerViewModel.getFlightBookingLuggageMetaViewModels()) {
                amenityViewModels.addAll(flightBookingAmenityMetaViewModel.getAmenities());
            }
            for (FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel : passengerViewModel.getFlightBookingMealMetaViewModels()) {
                amenityViewModels.addAll(flightBookingAmenityMetaViewModel.getAmenities());
            }
        }
        baseCartData.setAmenities(amenityViewModels);
        baseCartData.setTotal(getView().getCurrentBookingParamViewModel().getTotalPriceNumeric());
        baseCartData.setAdult(getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getAdult());
        baseCartData.setChild(getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getChildren());
        baseCartData.setInfant(getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getInfant());
        return baseCartData;
    }

    @Override
    void updateTotalPrice(int totalPrice, String totalPriceFmt) {
        getView().getCurrentBookingParamViewModel().setTotalPriceNumeric(totalPrice);
        getView().getCurrentBookingParamViewModel().setTotalPriceFmt(totalPriceFmt);
        getView().renderTotalPrices(totalPriceFmt);
    }

    @Override
    protected void onCountDownTimestimeChanged(String timestamp) {
        getView().getCurrentBookingParamViewModel().setOrderDueTimestamp(timestamp);
    }
}
