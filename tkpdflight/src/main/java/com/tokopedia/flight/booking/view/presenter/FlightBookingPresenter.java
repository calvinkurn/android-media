
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
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.common.constant.FlightErrorConstant;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightPassengerTitleType;
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

    private static final int TWELVE_YEARS_AGO = -12;
    private static final int MAX_CONTACT_NAME_LENGTH = 20;
    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;
    private FlightAddToCartUseCase flightAddToCartUseCase;
    private FlightBookingCartDataMapper flightBookingCartDataMapper;
    private FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase;
    private CompositeSubscription compositeSubscription;
    private FlightAnalytics flightAnalytics;
    private UserSession userSession;

    private static final int GENDER_MAN = 1;
    private static final int GENDER_WOMAN = 2;

    private boolean isChecked = false;

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

    public void renderUi(FlightBookingCartData flightBookingCartData, boolean isFromSavedInstance) {
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
        if (isFromSavedInstance) {
            getView().renderPassengersList(getView().getCurrentBookingParamViewModel().getPassengerViewModels());
            getView().setContactName(getView().getCurrentBookingParamViewModel().getContactName());
            getView().setContactEmail(getView().getCurrentBookingParamViewModel().getContactEmail());
            getView().setContactPhoneNumber(getView().getCurrentBookingParamViewModel().getContactPhone());
            Date expiredDate = getView().getExpiredTransactionDate();
            if (expiredDate != null) {
                getView().getCurrentBookingParamViewModel().setOrderDueTimestamp(FlightDateUtil.dateToString(expiredDate, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT));
                getView().renderFinishTimeCountDown(expiredDate);
            }
        } else {
            Date expiredDate = FlightDateUtil.addTimeToCurrentDate(Calendar.SECOND, flightBookingCartData.getRefreshTime());
            getView().getCurrentBookingParamViewModel().setOrderDueTimestamp(FlightDateUtil.dateToString(expiredDate, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT));
            getView().renderFinishTimeCountDown(expiredDate);
        }
        getView().getCurrentBookingParamViewModel().setPhoneCodeViewModel(flightBookingCartData.getDefaultPhoneCode());
        getView().renderPhoneCodeView(String.format("+%s", getView().getCurrentBookingParamViewModel().getPhoneCodeViewModel().getCountryPhoneCode()));

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


//        if (validatePassengerData()) {
//            toggleSameAsContactCheckbox();
//            onPassengerResultReceived(getPassengerViewModelFromContact());
//        }

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
        compositeSubscription.add(
                getDepartureDataObservable()
                        .map(new Func1<FlightSearchViewModel, FlightBookingCartData>() {
                            @Override
                            public FlightBookingCartData call(FlightSearchViewModel viewModel) {
                                FlightBookingCartData flightBookingCartData = new FlightBookingCartData();
                                FlightDetailViewModel flightDetailViewModel = new FlightDetailViewModel().build(viewModel);
                                flightDetailViewModel.build(getView().getCurrentBookingParamViewModel().getSearchParam());
                                flightBookingCartData.setDepartureTrip(flightDetailViewModel);
                                return flightBookingCartData;
                            }
                        })  .onBackpressureDrop()
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
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
                        actionGetReturnTripDataAndGetCart(flightBookingCartData);
                    }
                })

        );
        /*.flatMap(getFlightRoundTripDataObservable())
                .flatMap(new Func1<FlightBookingCartData, Observable<FlightBookingCartData>>() {
                    @Override
                    public Observable<FlightBookingCartData> call(FlightBookingCartData flightBookingCartData) {
                        List<Fare> fares = new ArrayList<>();
                        Fare departureFare = new Fare();
                        departureFare.setAdultNumeric(flightBookingCartData.getDepartureTrip().getAdultNumericPrice());
                        departureFare.setChildNumeric(flightBookingCartData.getDepartureTrip().getChildNumericPrice());
                        departureFare.setInfantNumeric(flightBookingCartData.getDepartureTrip().getInfantNumericPrice());
                        fares.add(departureFare);
                        if (flightBookingCartData.getReturnTrip() != null) {
                            Fare returnFare = new Fare();
                            returnFare.setAdultNumeric(flightBookingCartData.getReturnTrip().getAdultNumericPrice());
                            returnFare.setChildNumeric(flightBookingCartData.getReturnTrip().getChildNumericPrice());
                            returnFare.setInfantNumeric(flightBookingCartData.getReturnTrip().getInfantNumericPrice());
                            fares.add(returnFare);
                        }
                        FlightSearchPassDataViewModel searchPassDataViewModel = getView().getCurrentBookingParamViewModel().getSearchParam();
                        int price = calculateTotalPassengerFare(
                                fares,
                                searchPassDataViewModel.getFlightPassengerViewModel().getAdult(),
                                searchPassDataViewModel.getFlightPassengerViewModel().getChildren(),
                                searchPassDataViewModel.getFlightPassengerViewModel().getInfant()
                        );
                        return Observable.zip(flightAddToCartUseCase.createObservable(getRequestParams(price)), Observable.just(flightBookingCartData), new Func2<CartEntity, FlightBookingCartData, FlightBookingCartData>() {
                            @Override
                            public FlightBookingCartData call(CartEntity entity, FlightBookingCartData flightBookingCartData) {
                                return flightBookingCartDataMapper.transform(flightBookingCartData, entity);
                            }
                        });
                    }
                })
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
                .unsubscribeOn(Schedulers.io())
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
                            if (e instanceof FlightException && ((FlightException) e).getErrorList().contains(new FlightError(FlightErrorConstant.ADD_TO_CART))) {
                                getView().showExpireTransactionDialog(e.getMessage());
                            } else if (e instanceof FlightException && ((FlightException) e).getErrorList().contains(new FlightError(FlightErrorConstant.FLIGHT_SOLD_OUT))) {
                                getView().showSoldOutDialog();
                            } else {
                                getView().showGetCartDataErrorStateLayout(e);
                            }
                        }
                    }

                    @Override
                    public void onNext(FlightBookingCartData flightBookingCartData) {
                        if (isViewAttached()) {
                            flightAnalytics.eventAddToCart(flightBookingCartData.getDepartureTrip(), flightBookingCartData.getReturnTrip());
                            getView().hideFullPageLoading();
                            renderUi(flightBookingCartData, false);
                        }
                    }
                })*/
    }

    private void actionGetReturnTripDataAndGetCart(FlightBookingCartData flightBookingCartData) {
        compositeSubscription.add(
                Observable.just(flightBookingCartData)
                        .flatMap(getFlightRoundTripDataObservable())
                        .flatMap(new Func1<FlightBookingCartData, Observable<FlightBookingCartData>>() {
                            @Override
                            public Observable<FlightBookingCartData> call(FlightBookingCartData flightBookingCartData) {
                                List<Fare> fares = new ArrayList<>();
                                Fare departureFare = new Fare();
                                departureFare.setAdultNumeric(flightBookingCartData.getDepartureTrip().getAdultNumericPrice());
                                departureFare.setChildNumeric(flightBookingCartData.getDepartureTrip().getChildNumericPrice());
                                departureFare.setInfantNumeric(flightBookingCartData.getDepartureTrip().getInfantNumericPrice());
                                fares.add(departureFare);
                                if (flightBookingCartData.getReturnTrip() != null) {
                                    Fare returnFare = new Fare();
                                    returnFare.setAdultNumeric(flightBookingCartData.getReturnTrip().getAdultNumericPrice());
                                    returnFare.setChildNumeric(flightBookingCartData.getReturnTrip().getChildNumericPrice());
                                    returnFare.setInfantNumeric(flightBookingCartData.getReturnTrip().getInfantNumericPrice());
                                    fares.add(returnFare);
                                }
                                FlightSearchPassDataViewModel searchPassDataViewModel = getView().getCurrentBookingParamViewModel().getSearchParam();
                                int price = calculateTotalPassengerFare(
                                        fares,
                                        searchPassDataViewModel.getFlightPassengerViewModel().getAdult(),
                                        searchPassDataViewModel.getFlightPassengerViewModel().getChildren(),
                                        searchPassDataViewModel.getFlightPassengerViewModel().getInfant()
                                );
                                return Observable.zip(flightAddToCartUseCase.createObservable(getRequestParams(price)), Observable.just(flightBookingCartData), new Func2<CartEntity, FlightBookingCartData, FlightBookingCartData>() {
                                    @Override
                                    public FlightBookingCartData call(CartEntity entity, FlightBookingCartData flightBookingCartData) {
                                        return flightBookingCartDataMapper.transform(flightBookingCartData, entity);
                                    }
                                });
                            }
                        })
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
                        .unsubscribeOn(Schedulers.io())
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
                                    if (e instanceof FlightException && ((FlightException) e).getErrorList().contains(new FlightError(FlightErrorConstant.ADD_TO_CART))) {
                                        getView().showExpireTransactionDialog(e.getMessage());
                                    } else if (e instanceof FlightException && ((FlightException) e).getErrorList().contains(new FlightError(FlightErrorConstant.FLIGHT_SOLD_OUT))) {
                                        getView().showSoldOutDialog();
                                    } else {
                                        getView().showGetCartDataErrorStateLayout(e);
                                    }
                                }
                            }

                            @Override
                            public void onNext(FlightBookingCartData flightBookingCartData) {
                                if (isViewAttached()) {
                                    flightAnalytics.eventAddToCart(flightBookingCartData.getDepartureTrip(), flightBookingCartData.getReturnTrip());
                                    getView().hideFullPageLoading();
                                    renderUi(flightBookingCartData, false);
                                }
                            }
                        })
        );
    }

    private RequestParams getRequestParams(int price) {
        RequestParams requestParams;
        if (isRoundTrip()) {
            requestParams = flightAddToCartUseCase.createRequestParam(
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getAdult(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getChildren(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getInfant(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getReturnTripId(),
                    getView().getIdEmpotencyKey(getView().getDepartureTripId() + "_" + getView().getReturnTripId()),
                    price
            );
        } else {
            requestParams = flightAddToCartUseCase.createRequestParam(
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getAdult(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getChildren(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightPassengerViewModel().getInfant(),
                    getView().getCurrentBookingParamViewModel().getSearchParam().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getIdEmpotencyKey(getView().getDepartureTripId()),
                    price
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
    public void onChangePassengerButtonClicked(FlightBookingPassengerViewModel viewModel,
                                               String departureDate) {
        String requestId;
        if (getView().getReturnTripId() != null && getView().getReturnTripId().length() > 0) {
            requestId = getView().getIdEmpotencyKey(
                    getView().getDepartureTripId() + "_" + getView().getReturnTripId()
            );
        } else {
            requestId = getView().getIdEmpotencyKey(
                    getView().getDepartureTripId()
            );
        }
        getView().navigateToPassengerInfoDetail(viewModel, isMandatoryDoB(), departureDate, requestId);
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
                            getView().setContactBirthdate(
                                    FlightDateUtil.dateToString(
                                            FlightDateUtil.stringToDate(profileInfo.getBday()),
                                            FlightDateUtil.DEFAULT_FORMAT
                                    )
                            );
                            getView().setContactGender(profileInfo.getGender());
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
            viewModel.setPassengerLocalId(passengerNumber);
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
            viewModel.setPassengerLocalId(passengerNumber);
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
            viewModel.setPassengerLocalId(passengerNumber);
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
        return getRequestParams(calculateTotalPassengerFare());
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
    protected void onCountDownTimestampChanged(String timestamp) {
        getView().getCurrentBookingParamViewModel().setOrderDueTimestamp(timestamp);
    }

    @Override
    public void toggleSameAsContactCheckbox() {
        if (isChecked) {
            isChecked = false;
            uncheckSameAsContact();
        } else {
            isChecked = true;
        }

        getView().setSameAsContactChecked(isChecked);

    }

    @Override
    public void onSameAsContactClicked(boolean navigateToPassengerInfo) {
        if (isChecked && !getView().getContactName().isEmpty() && getView().getContactName().length() > 0) {


            if (validatePassengerData()) {
                FlightBookingPassengerViewModel flightBookingPassengerViewModel = getPassengerViewModelFromContact();
                String departureDate;
                FlightBookingParamViewModel paramViewModel = getView().getCurrentBookingParamViewModel();

                if (!paramViewModel.getSearchParam().getReturnDate().equals("") &&
                        paramViewModel.getSearchParam().getReturnDate() != null) {
                    departureDate = paramViewModel.getSearchParam().getReturnDate();
                } else {
                    departureDate = paramViewModel.getSearchParam().getDepartureDate();
                }

                if (navigateToPassengerInfo) {
                    onChangePassengerButtonClicked(flightBookingPassengerViewModel, departureDate);
                }
            } else {
                toggleSameAsContactCheckbox();
            }
        }
    }

    private boolean isMandatoryDoB() {
        FlightBookingCartData flightBookingCartData = getView().getCurrentCartPassData();

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

    private boolean validatePassengerData() {
        boolean isValid = true;

        Date twelveYearsAgo = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, TWELVE_YEARS_AGO);

        if (getView().getContactName().isEmpty() || getView().getContactName().length() == 0) {
            isValid = false;
            getView().showContactNameEmptyError(R.string.flight_booking_checkbox_same_as_contact_name_empty_error);
        } else if (getView().getContactName().length() > MAX_CONTACT_NAME_LENGTH) {
            isValid = false;
            getView().showContactNameInvalidError(R.string.flight_booking_contact_name_max_length_error);
        } else if (getView().getContactName().length() > 0 && !isAlphabetAndSpaceOnly(getView().getContactName())) {
            isValid = false;
            getView().showContactNameInvalidError(R.string.flight_booking_contact_name_alpha_space_error);
        } else if (isMandatoryDoB() &&
                FlightDateUtil.stringToDate(getView().getContactBirthdate()).after(twelveYearsAgo)) {
            isValid = false;
        }

        return isValid;
    }

    private FlightBookingPassengerViewModel getPassengerViewModelFromContact() {
        int lastIndexOfSpace = getView().getContactName().lastIndexOf(" ");

        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getView()
                .getCurrentBookingParamViewModel()
                .getPassengerViewModels().get(0);
        flightBookingPassengerViewModel.setFlightBookingLuggageMetaViewModels(
                new ArrayList<FlightBookingAmenityMetaViewModel>()
        );
        flightBookingPassengerViewModel.setFlightBookingMealMetaViewModels(
                new ArrayList<FlightBookingAmenityMetaViewModel>()
        );

        if (lastIndexOfSpace > 0) {
            flightBookingPassengerViewModel.setPassengerFirstName(
                    getView().getContactName().substring(
                            0,
                            lastIndexOfSpace
                    ).trim()
            );
            flightBookingPassengerViewModel.setPassengerLastName(
                    getView().getContactName().substring(lastIndexOfSpace)
                            .trim()
            );
        } else {
            flightBookingPassengerViewModel.setPassengerFirstName(getView().getContactName().trim());
            flightBookingPassengerViewModel.setPassengerLastName(getView().getContactName().trim());
        }

        if (isMandatoryDoB()) {
            flightBookingPassengerViewModel.setPassengerBirthdate(getView().getContactBirthdate());
        }

        if (getView().getContactGender() == GENDER_MAN) {
            flightBookingPassengerViewModel.setPassengerTitle(getView().getString(R.string.mister));
            flightBookingPassengerViewModel.setPassengerTitleId(FlightPassengerTitleType.TUAN);
        } else if (getView().getContactGender() == GENDER_WOMAN) {
            flightBookingPassengerViewModel.setPassengerTitle(getView().getString(R.string.misiz));
            flightBookingPassengerViewModel.setPassengerTitleId(FlightPassengerTitleType.NYONYA);
        }

        return flightBookingPassengerViewModel;
    }

    private void uncheckSameAsContact() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getView()
                .getCurrentBookingParamViewModel()
                .getPassengerViewModels()
                .get(0);
        flightBookingPassengerViewModel.setPassengerFirstName(null);
        flightBookingPassengerViewModel.setPassengerLastName(null);
        flightBookingPassengerViewModel.setPassengerTitle(null);
        flightBookingPassengerViewModel.setPassengerTitleId(0);
        flightBookingPassengerViewModel.setPassengerBirthdate(null);
        flightBookingPassengerViewModel.setFlightBookingLuggageMetaViewModels(
                new ArrayList<FlightBookingAmenityMetaViewModel>()
        );
        flightBookingPassengerViewModel.setFlightBookingMealMetaViewModels(
                new ArrayList<FlightBookingAmenityMetaViewModel>()
        );

        onPassengerResultReceived(flightBookingPassengerViewModel);
    }
}
