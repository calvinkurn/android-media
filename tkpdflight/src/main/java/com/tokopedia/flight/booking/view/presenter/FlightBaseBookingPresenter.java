package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.common.constant.FlightErrorConstant;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.search.data.cloud.model.response.Fare;
import com.tokopedia.usecase.RequestParams;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author by alvarisi on 11/29/17.
 */

public abstract class FlightBaseBookingPresenter<T extends FlightBaseBookingContact.View> extends BaseDaggerPresenter<T>
        implements FlightBaseBookingContact.Presenter<T> {
    protected FlightAddToCartUseCase addToCartUseCase;
    private FlightBookingCartDataMapper flightBookingCartDataMapper;

    public FlightBaseBookingPresenter(FlightAddToCartUseCase addToCartUseCase, FlightBookingCartDataMapper flightBookingCartDataMapper) {
        this.addToCartUseCase = addToCartUseCase;
        this.flightBookingCartDataMapper = flightBookingCartDataMapper;
    }

    protected abstract RequestParams getRequestParam();

    protected abstract BaseCartData getCurrentCartData();

    protected abstract void updateTotalPrice(int totalPrice);

    protected abstract void onCountDownTimestimeChanged(String timestamp);

    @Override
    public void onUpdateCart() {
        getView().showUpdatePriceLoading();
        addToCartUseCase.createObservable(getRequestParam())
                .map(new Func1<CartEntity, FlightBookingCartData>() {
                    @Override
                    public FlightBookingCartData call(CartEntity entity) {
                        return flightBookingCartDataMapper.transform(entity);
                    }
                })
                .map(new Func1<FlightBookingCartData, BaseCartData>() {
                    @Override
                    public BaseCartData call(FlightBookingCartData flightBookingCartData) {
                        BaseCartData baseCartData = cloneViewModel(getCurrentCartData());
                        List<Fare> fares = new ArrayList<>();
                        for (NewFarePrice newFare : flightBookingCartData.getNewFarePrices()) {
                            fares.add(newFare.getFare());
                        }
                        int newTotalPrice = calculateTotalFareAndAmenities(
                                fares,
                                baseCartData.getAdult(),
                                baseCartData.getChild(),
                                baseCartData.getInfant(),
                                baseCartData.getAmenities()
                        );
                        baseCartData.setRefreshTime(flightBookingCartData.getRefreshTime());
                        baseCartData.setNewFarePrices(flightBookingCartData.getNewFarePrices());
                        baseCartData.setTotal(newTotalPrice);
                        return baseCartData;
                    }
                })
                .onBackpressureDrop()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseCartData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().hideUpdatePriceLoading();
                            getView().showUpdateDataErrorStateLayout(FlightErrorUtil.getMessageFromException(e));
                            if (e instanceof FlightException && ((FlightException) e).getErrorList().contains(new FlightError(FlightErrorConstant.ADD_TO_CART))){
                                getView().showExpireTransactionDialog();
                            }
                        }
                    }

                    @Override
                    public void onNext(BaseCartData baseCartData) {
                        getView().hideUpdatePriceLoading();

                        Date expiredDate = FlightDateUtil.addTimeToCurrentDate(Calendar.SECOND, baseCartData.getRefreshTime());
                        getView().renderFinishTimeCountDown(expiredDate);
                        onCountDownTimestimeChanged(FlightDateUtil.dateToString(expiredDate, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT));

                        if (baseCartData.getTotal() != getCurrentCartData().getTotal()) {
                            getView().showPriceChangesDialog(convertPriceValueToIdrFormat(baseCartData.getTotal()),
                                    convertPriceValueToIdrFormat(getCurrentCartData().getTotal()));
                            updateTotalPrice(baseCartData.getTotal());
                            actionCalculatePriceAndRender(
                                    baseCartData.getNewFarePrices(),
                                    getView().getDepartureFlightDetailViewModel(),
                                    getView().getReturnFlightDetailViewModel(),
                                    getView().getFlightBookingPassengers()
                            );
                        }

                    }
                });
    }

    protected int calculateTotalFareAndAmenities(List<Fare> newFares, int adult, int child, int infant, List<FlightBookingAmenityViewModel> amenities) {
        int newTotalPrice = 0;
        for (Fare newFare : newFares) {
            newTotalPrice += newFare.getAdultNumeric() * adult;
            newTotalPrice += newFare.getChildNumeric() * child;
            newTotalPrice += newFare.getInfantNumeric() * infant;
        }

        for (FlightBookingAmenityViewModel amenityViewModel : amenities) {
            newTotalPrice += amenityViewModel.getPriceNumeric();
        }
        return newTotalPrice;
    }

    @Nullable
    private BaseCartData cloneViewModel(BaseCartData currentDashboardViewModel) {
        BaseCartData viewModel = null;
        try {
            viewModel = (BaseCartData) currentDashboardViewModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Clone FlightDashboardViewModel");
        }
        return viewModel;
    }

    protected String convertPriceValueToIdrFormat(int price) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String result = kursIndonesia.format(price);

        return result.replace(",", ".");
    }

    protected void actionCalculatePriceAndRender(
            List<NewFarePrice> newFarePrices,
            FlightDetailViewModel departureDetailViewModel,
            FlightDetailViewModel returnDetailViewModel,
            List<FlightBookingPassengerViewModel> flightBookingPassengers) {
        for (NewFarePrice newFarePrice : newFarePrices) {
            if (newFarePrice.getId().equalsIgnoreCase(departureDetailViewModel.getId())) {
                departureDetailViewModel.setAdultNumericPrice(newFarePrice.getFare().getAdultNumeric());
                departureDetailViewModel.setChildNumericPrice(newFarePrice.getFare().getChildNumeric());
                departureDetailViewModel.setInfantNumericPrice(newFarePrice.getFare().getInfantNumeric());
            }
            if (returnDetailViewModel != null && newFarePrice.getId().equalsIgnoreCase(returnDetailViewModel.getId())) {
                returnDetailViewModel.setAdultNumericPrice(newFarePrice.getFare().getAdultNumeric());
                returnDetailViewModel.setChildNumericPrice(newFarePrice.getFare().getChildNumeric());
                returnDetailViewModel.setInfantNumericPrice(newFarePrice.getFare().getInfantNumeric());
            }
        }
        List<SimpleViewModel> simpleViewModels = new ArrayList<>();
        if (departureDetailViewModel.getAdultNumericPrice() > 0) {
            simpleViewModels.add(
                    formatPassengerFarePriceDetail(
                            departureDetailViewModel.getDepartureAirport(),
                            departureDetailViewModel.getArrivalAirport(),
                            getView().getString(R.string.flightbooking_price_adult_label),
                            departureDetailViewModel.getCountAdult(),
                            departureDetailViewModel.getAdultNumericPrice() * departureDetailViewModel.getCountAdult()
                    )
            );
        }
        if (departureDetailViewModel.getCountChild() > 0
                && departureDetailViewModel.getChildNumericPrice() > 0) {
            simpleViewModels.add(
                    formatPassengerFarePriceDetail(
                            departureDetailViewModel.getDepartureAirport(),
                            departureDetailViewModel.getArrivalAirport(),
                            getView().getString(R.string.flightbooking_price_child_label),
                            departureDetailViewModel.getCountChild(),
                            departureDetailViewModel.getChildNumericPrice() * departureDetailViewModel.getCountChild()
                    )
            );
        }
        if (departureDetailViewModel.getCountInfant() > 0
                && departureDetailViewModel.getInfantNumericPrice() > 0) {
            simpleViewModels.add(
                    formatPassengerFarePriceDetail(
                            departureDetailViewModel.getDepartureAirport(),
                            departureDetailViewModel.getArrivalAirport(),
                            getView().getString(R.string.flightbooking_price_infant_label),
                            departureDetailViewModel.getCountInfant(),
                            departureDetailViewModel.getInfantNumericPrice() * departureDetailViewModel.getCountInfant()
                    )
            );
        }

        if (returnDetailViewModel != null) {
            if (returnDetailViewModel.getAdultNumericPrice() > 0) {
                simpleViewModels.add(
                        formatPassengerFarePriceDetail(
                                returnDetailViewModel.getDepartureAirport(),
                                returnDetailViewModel.getArrivalAirport(),
                                getView().getString(R.string.flightbooking_price_adult_label),
                                returnDetailViewModel.getCountAdult(),
                                returnDetailViewModel.getAdultNumericPrice() * returnDetailViewModel.getCountAdult()
                        )
                );
            }
            if (returnDetailViewModel.getCountChild() > 0
                    && returnDetailViewModel.getChildNumericPrice() > 0) {
                simpleViewModels.add(
                        formatPassengerFarePriceDetail(
                                returnDetailViewModel.getDepartureAirport(),
                                returnDetailViewModel.getArrivalAirport(),
                                getView().getString(R.string.flightbooking_price_child_label),
                                returnDetailViewModel.getCountChild(),
                                returnDetailViewModel.getChildNumericPrice() * returnDetailViewModel.getCountChild()
                        )
                );
            }
            if (returnDetailViewModel.getCountInfant() > 0
                    && returnDetailViewModel.getInfantNumericPrice() > 0) {
                simpleViewModels.add(
                        formatPassengerFarePriceDetail(
                                returnDetailViewModel.getDepartureAirport(),
                                returnDetailViewModel.getArrivalAirport(),
                                getView().getString(R.string.flightbooking_price_infant_label),
                                returnDetailViewModel.getCountInfant(),
                                returnDetailViewModel.getInfantNumericPrice() * returnDetailViewModel.getCountInfant()
                        )
                );
            }
        }
        for (FlightBookingPassengerViewModel flightPassengerViewModel : flightBookingPassengers) {
            for (FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel : flightPassengerViewModel.getFlightBookingMealMetaViewModels()) {
                for (FlightBookingAmenityViewModel flightBookingAmenityViewModel : flightBookingAmenityMetaViewModel.getAmenities()) {
                    simpleViewModels.add(
                            new SimpleViewModel(
                                    String.format("%s %s", getView().getString(R.string.flight_price_detail_prefixl_meal_label), flightBookingAmenityMetaViewModel.getDescription()),
                                    flightBookingAmenityViewModel.getPrice())
                    );
                }
            }
            for (FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel : flightPassengerViewModel.getFlightBookingLuggageMetaViewModels()) {
                for (FlightBookingAmenityViewModel flightBookingLuggageViewModel : flightBookingLuggageMetaViewModel.getAmenities()) {
                    simpleViewModels.add(new SimpleViewModel(
                            String.format("%s %s", getView().getString(R.string.flight_price_detail_prefix_luggage_label), flightBookingLuggageMetaViewModel.getDescription()),
                            flightBookingLuggageViewModel.getPrice())
                    );
                }
            }
        }

        getView().renderPriceListDetails(simpleViewModels);
    }

    private SimpleViewModel formatPassengerFarePriceDetail(String departureAirport,
                                                           String arrivalAirport,
                                                           String label,
                                                           int passengerCount,
                                                           int price) {
        return new SimpleViewModel(
                String.format(getView().getString(R.string.flight_booking_passenger_price_format),
                        departureAirport,
                        arrivalAirport,
                        passengerCount,
                        label),
                convertPriceValueToIdrFormat(price));
    }
}
