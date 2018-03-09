package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.domain.FlightBookingGetPhoneCodeUseCase;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;
    private FlightAddToCartUseCase flightAddToCartUseCase;
    private FlightBookingCartDataMapper flightBookingCartDataMapper;
    private FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase;
    private CompositeSubscription compositeSubscription;
    private FlightAnalytics flightAnalytics;
    private UserSession userSession;

    @Inject
    public FlightBookingPresenter(FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase,
                                  FlightAddToCartUseCase flightAddToCartUseCase,
                                  FlightBookingCartDataMapper flightBookingCartDataMapper,
                                  FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase,
                                  FlightAnalytics flightAnalytics,
                                  UserSession userSession) {
        super(flightAddToCartUseCase, flightBookingCartDataMapper);
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
        this.flightAddToCartUseCase = flightAddToCartUseCase;
        this.flightBookingCartDataMapper = flightBookingCartDataMapper;
        this.flightBookingGetPhoneCodeUseCase = flightBookingGetPhoneCodeUseCase;
        this.flightAnalytics = flightAnalytics;
        this.userSession = userSession;
        this.compositeSubscription = new CompositeSubscription();
    }

    public static String transform(String phoneRawString) {
        phoneRawString = checkStart(phoneRawString);
        phoneRawString = phoneRawString.replace("-", "");
        StringBuilder phoneNumArr = new StringBuilder(phoneRawString);
        if (phoneNumArr.length() > 0) {
            phoneNumArr.replace(0, 1, "");
        }
        return phoneNumArr.toString();
    }

    private static String checkStart(String phoneRawString) {
        if (phoneRawString.startsWith("62")) {
            phoneRawString = phoneRawString.replaceFirst("62", "0");
        } else if (phoneRawString.startsWith("+62")) {
            phoneRawString = phoneRawString.replaceFirst("\\+62", "0");
        }
        return phoneRawString;
    }

    @Override
    public void onButtonSubmitClicked() {
        if (validateFields()) {
            flightAnalytics.eventBookingNextClick(getView().getString(R.string.flight_booking_analytics_customer_page));
            getView().getCurrentBookingParamViewModel().setContactName(getView().getContactName());
            getView().getCurrentBookingParamViewModel().setContactEmail(getView().getContactEmail());
            getView().getCurrentBookingParamViewModel().setContactPhone(getView().getContactPhoneNumber());
            FlightBookingReviewModel flightBookingReviewModel =
                    new FlightBookingReviewModel(
                            getView().getCurrentBookingParamViewModel(),
                            getView().getCurrentCartPassData(),
                            getView().getDepartureTripId(),
                            getView().getReturnTripId(),
                            getView().getString(R.string.flight_luggage_prefix),
                            getView().getString(R.string.flight_meal_prefix),
                            getView().getString(R.string.flight_birthdate_prefix)
                    );
            getView().navigateToReview(flightBookingReviewModel);
        }
    }

    private void renderUi(FlightBookingCartData flightBookingCartData) {
        getView().getCurrentBookingParamViewModel().setId(flightBookingCartData.getId());
        getView().setCartData(flightBookingCartData);
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
        int oldTotalPrice = actionCalculateCurrentTotalPrice(flightBookingCartData.getDepartureTrip(), flightBookingCartData.getReturnTrip());
        int resultTotalPrice = 0;
        resultTotalPrice = oldTotalPrice;

        if (flightBookingCartData.getNewFarePrices() != null && flightBookingCartData.getNewFarePrices().size() > 0) {
            for (NewFarePrice newFarePrice : flightBookingCartData.getNewFarePrices()) {
                if (newFarePrice.getId().equalsIgnoreCase(flightBookingCartData.getDepartureTrip().getId())) {
                    flightBookingCartData.getDepartureTrip().setAdultNumericPrice(newFarePrice.getFare().getAdultNumeric());
                    flightBookingCartData.getDepartureTrip().setChildNumericPrice(newFarePrice.getFare().getChildNumeric());
                    flightBookingCartData.getDepartureTrip().setInfantNumericPrice(newFarePrice.getFare().getInfantNumeric());
                } else if (isRoundTrip() && newFarePrice.getId().equalsIgnoreCase(flightBookingCartData.getReturnTrip().getId())) {
                    flightBookingCartData.getReturnTrip().setAdultNumericPrice(newFarePrice.getFare().getAdultNumeric());
                    flightBookingCartData.getReturnTrip().setChildNumericPrice(newFarePrice.getFare().getChildNumeric());
                    flightBookingCartData.getReturnTrip().setInfantNumericPrice(newFarePrice.getFare().getInfantNumeric());
                }
            }
            int newTotalPrice = actionCalculateCurrentTotalPrice(flightBookingCartData.getDepartureTrip(), flightBookingCartData.getReturnTrip());
            if (newTotalPrice != oldTotalPrice) {
                resultTotalPrice = newTotalPrice;
                getView().showPriceChangesDialog(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(resultTotalPrice), CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(oldTotalPrice));
            }
        }
        updateTotalPrice(resultTotalPrice);

        actionCalculatePriceAndRender(flightBookingCartData.getNewFarePrices(),
                flightBookingCartData.getDepartureTrip(),
                flightBookingCartData.getReturnTrip(),
                getView().getCurrentBookingParamViewModel().getPassengerViewModels()
        );

    }

    private int actionCalculateCurrentTotalPrice(FlightDetailViewModel departureFlightDetailViewModel, FlightDetailViewModel returnFlightDetailViewModel) {
        BaseCartData baseCartData = getCurrentCartData();
        List<Fare> fares = new ArrayList<>();
        fares.add(
                new Fare(
                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(departureFlightDetailViewModel.getAdultNumericPrice()),
                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(departureFlightDetailViewModel.getChildNumericPrice()),
                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(departureFlightDetailViewModel.getInfantNumericPrice()),
                        departureFlightDetailViewModel.getAdultNumericPrice(),
                        departureFlightDetailViewModel.getChildNumericPrice(),
                        departureFlightDetailViewModel.getInfantNumericPrice()
                )
        );
        if (returnFlightDetailViewModel != null) {
            fares.add(
                    new Fare(
                            CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(returnFlightDetailViewModel.getAdultNumericPrice()),
                            CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(returnFlightDetailViewModel.getChildNumericPrice()),
                            CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(returnFlightDetailViewModel.getInfantNumericPrice()),
                            returnFlightDetailViewModel.getAdultNumericPrice(),
                            returnFlightDetailViewModel.getChildNumericPrice(),
                            returnFlightDetailViewModel.getInfantNumericPrice()
                    )
            );
        }

        return calculateTotalFareAndAmenities(
                fares,
                baseCartData.getAdult(),
                baseCartData.getChild(),
                baseCartData.getInfant(),
                baseCartData.getAmenities()
        );
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

        int newTotalPrice = actionCalculateCurrentTotalPrice(
                getView().getDepartureFlightDetailViewModel(),
                getView().getReturnFlightDetailViewModel()
        );
        updateTotalPrice(newTotalPrice);
    }

    @Override
    public void onDepartureInfoClicked() {
        flightAnalytics.eventDetailClick(getView().getCurrentCartPassData().getDepartureTrip());
        getView().navigateToDetailTrip(getView().getCurrentCartPassData().getDepartureTrip());
    }

    @Override
    public void onReturnInfoClicked() {
        flightAnalytics.eventDetailClick(getView().getCurrentCartPassData().getDepartureTrip());
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
                .subscribeOn(Schedulers.io())
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
                            getView().showGetCartDataErrorStateLayout(e);
                        }
                    }

                    @Override
                    public void onNext(FlightBookingCartData flightBookingCartData) {
                        if (isViewAttached()) {
                            flightAnalytics.eventAddToCart(flightBookingCartData.getDepartureTrip(), flightBookingCartData.getReturnTrip());
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
                    getView().getIdEmpotencyKey(getView().getDepartureTripId() + "_" + getView().getReturnTripId())
            );
        } else {
            requestParams = flightAddToCartUseCase.createRequestParam(
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getAdult(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getChildren(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getInfant(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightClass().getId(),
                    getView().getDepartureTripId(),
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
        return flightBookingGetPhoneCodeUseCase.createObservable(flightBookingGetPhoneCodeUseCase.createRequest("Indonesia"))
                .flatMap(new Func1<List<FlightBookingPhoneCodeViewModel>, Observable<FlightBookingPhoneCodeViewModel>>() {
                    @Override
                    public Observable<FlightBookingPhoneCodeViewModel> call(List<FlightBookingPhoneCodeViewModel> flightBookingPhoneCodeViewModels) {
                        return Observable.from(flightBookingPhoneCodeViewModels);
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
    public void onChangePassengerButtonClicked(FlightBookingPassengerViewModel viewModel, FlightBookingCartData cartData, String departureDate) {
        getView().navigateToPassengerInfoDetail(viewModel, isAirAsiaAirline(cartData), departureDate);
    }

    @Override
    public void onGetProfileData() {
        compositeSubscription.add(getView().getProfileObservable()
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProfileInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {

                        }

                    }

                    @Override
                    public void onNext(ProfileInfo profileInfo) {
                        if (profileInfo != null && isViewAttached()) {
                            if (getView().getContactName().length() == 0) {
                                getView().setContactName(profileInfo.getFullname());
                            }
                            if (getView().getContactEmail().length() == 0) {
                                getView().setContactEmail(profileInfo.getEmail());
                            }
                            if (getView().getContactPhoneNumber().length() == 0) {
                                getView().setContactPhoneNumber(transform(profileInfo.getPhoneNumber()));
                            }
                        }
                    }
                })
        );
    }

    @Override
    public void initialize() {
        if (userSession.isMsisdnVerified()) {
            processGetCartData();
            onGetProfileData();
        } else {
            getView().navigateToOtpPage();
        }
    }

    @Override
    public void onReceiveOtpSuccessResult() {
        initialize();
    }

    @Override
    public void onReceiveOtpCancelResult() {
        getView().closePage();
    }

    @Override
    public void onRetryGetCartData() {
        processGetCartData();
    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        detachView();
    }

    @Override
    public void onFinishTransactionTimeReached() {
        if (isViewAttached()) {
            onUpdateCart();
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
        int passengerNumber = 1;
        List<FlightBookingPassengerViewModel> viewModels = new ArrayList<>();
        for (int i = 1, adultTotal = passData.getFlightPassengerViewModel().getAdult(); i <= adultTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setPassengerId(passengerNumber);
            viewModel.setType(FlightBookingPassenger.ADULT);
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
        } else if (getView().getContactName().length() > 0 && !isAlphabetAndSpaceOnly(getView().getContactName())) {
            isValid = false;
            getView().showContactNameInvalidError(R.string.flight_booking_contact_name_alpha_space_error);
        } else if (getView().getContactEmail().length() == 0) {
            isValid = false;
            getView().showContactEmailEmptyError(R.string.flight_booking_contact_email_empty_error);
        } else if (!isValidEmail(getView().getContactEmail())) {
            isValid = false;
            getView().showContactEmailInvalidError(R.string.flight_booking_contact_email_invalid_error);
        } else if (!isEmailWithoutProhibitSymbol(getView().getContactEmail())) {
            isValid = false;
            getView().showContactEmailInvalidSymbolError(R.string.flight_booking_contact_email_invalid_symbol_error);
        } else if (getView().getContactPhoneNumber().length() == 0) {
            isValid = false;
            getView().showContactPhoneNumberEmptyError(R.string.flight_booking_contact_phone_empty_error);
        } else if (getView().getContactPhoneNumber().length() > 0 && !isNumericOnly(getView().getContactPhoneNumber())) {
            isValid = false;
            getView().showContactPhoneNumberInvalidError(R.string.flight_booking_contact_phone_invalid_error);
        } else if (getView().getContactPhoneNumber().length() > 13) {
            isValid = false;
            getView().showContactPhoneNumberInvalidError(R.string.flight_booking_contact_phone_max_length_error);
        } else if (getView().getContactPhoneNumber().length() < 9) {
            isValid = false;
            getView().showContactPhoneNumberInvalidError(R.string.flight_booking_contact_phone_min_length_error);
        } else if (!isAllPassengerFilled(getView().getCurrentBookingParamViewModel().getPassengerViewModels())) {
            isValid = false;
            getView().showPassengerInfoNotFullfilled(R.string.flight_booking_passenger_not_fullfilled_error);
        }
        return isValid;
    }

    private boolean isEmailWithoutProhibitSymbol(String contactEmail) {
        return !contactEmail.contains("+");
    }

    private boolean isAllPassengerFilled(List<FlightBookingPassengerViewModel> passengerViewModels) {
        boolean isvalid = true;
        for (FlightBookingPassengerViewModel flightBookingPassengerViewModel : passengerViewModels) {
            if (flightBookingPassengerViewModel.getPassengerFirstName() == null) {
                isvalid = false;
                break;
            }
        }
        return isvalid;
    }

    private boolean isNumericOnly(String expression) {
        Pattern pattern = Pattern.compile(new String("^[0-9\\s]*$"));
        Matcher matcher = pattern.matcher(expression);
        return matcher.matches();
    }

    private boolean isAlphabetAndSpaceOnly(String expression) {
        Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(expression);
        return matcher.matches();
    }

    private boolean isValidEmail(String contactEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() && !contactEmail.contains(".@") && !contactEmail.contains("@.");
    }

    @Override
    protected RequestParams getRequestParam() {
        return getRequestParams();
    }

    @Override
    protected BaseCartData getCurrentCartData() {
        BaseCartData baseCartData = new BaseCartData();
        baseCartData.setId(getView().getCurrentCartPassData().getId());
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
    protected void updateTotalPrice(int totalPrice) {
        getView().getCurrentBookingParamViewModel().setTotalPriceNumeric(totalPrice);
        getView().getCurrentBookingParamViewModel().setTotalPriceFmt(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(totalPrice));
        getView().renderTotalPrices(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(totalPrice));
    }

    @Override
    protected void onCountDownTimestimeChanged(String timestamp) {
        getView().getCurrentBookingParamViewModel().setOrderDueTimestamp(timestamp);
    }

    private boolean isAirAsiaAirline(FlightBookingCartData flightBookingCartData) {

        if (flightBookingCartData.getDepartureTrip() != null)
            for (FlightDetailRouteViewModel data : flightBookingCartData.getDepartureTrip().getRouteList()) {
                if (data.isAirlineMandatoryDOB() == 1)
                    return true;
            }

        if (flightBookingCartData.getReturnTrip() != null)
            for (FlightDetailRouteViewModel data : flightBookingCartData.getReturnTrip().getRouteList()) {
                if (data.isAirlineMandatoryDOB() == 1)
                    return true;
            }

        return false;
    }
}
