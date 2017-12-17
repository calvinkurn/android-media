package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.view.presenter.FlightBaseBookingPresenter;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.domain.FlightBookingCheckoutUseCase;
import com.tokopedia.flight.review.domain.FlightBookingVerifyUseCase;
import com.tokopedia.flight.review.domain.FlightCheckVoucherCodeUseCase;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPresenter extends FlightBaseBookingPresenter<FlightBookingReviewContract.View> implements FlightBookingReviewContract.Presenter {

    private final FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase;
    private final FlightBookingCheckoutUseCase flightBookingCheckoutUseCase;
    private final FlightBookingVerifyUseCase flightBookingVerifyUseCase;

    @Inject
    public FlightBookingReviewPresenter(FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase,
                                        FlightBookingCheckoutUseCase flightBookingCheckoutUseCase,
                                        FlightAddToCartUseCase flightAddToCartUseCase,
                                        FlightBookingCartDataMapper flightBookingCartDataMapper,
                                        FlightBookingVerifyUseCase flightBookingVerifyUseCase) {
        super(flightAddToCartUseCase, flightBookingCartDataMapper);
        this.flightCheckVoucherCodeUseCase = flightCheckVoucherCodeUseCase;
        this.flightBookingCheckoutUseCase = flightBookingCheckoutUseCase;
        this.flightBookingVerifyUseCase = flightBookingVerifyUseCase;
    }

    @Override
    public void verifyBooking(String promoCode, int price, int adult, String cartId,
                              List<FlightBookingPassengerViewModel> flightPassengerViewModels,
                              String contactName, String country, String email, String phone) {
        getView().showProgressDialog();
        flightBookingVerifyUseCase.execute(flightBookingVerifyUseCase.createRequestParams(promoCode,
                price, adult, cartId, flightPassengerViewModels, contactName, country, email, phone)
                , getSubscriberVerifyBooking());
        /*flightBookingVerifyUseCase.createObservable(
                flightBookingVerifyUseCase.createRequestParams(
                        promoCode,
                        price,
                        adult,
                        cartId,
                        flightPassengerViewModels,
                        contactName,
                        country,
                        email,
                        phone
                )
        );*/
    }

    @Override
    public void checkVoucherCode(String cartId, String voucherCode) {
        getView().showProgressDialog();
        flightCheckVoucherCodeUseCase.execute(flightCheckVoucherCodeUseCase.createRequestParams(cartId, voucherCode), getSubscriberCheckVoucherCode());
    }

    @Override
    public void submitData() {
        flightBookingCheckoutUseCase.execute(RequestParams.create(), getSubscriberSubmitData());
    }

    private Subscriber<DataResponseVerify> getSubscriberVerifyBooking() {
        return new Subscriber<DataResponseVerify>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideProgressDialog();
                    getView().onErrorVerifyCode(e);
                }
            }

            @Override
            public void onNext(DataResponseVerify dataResponseVerify) {
                getView().hideProgressDialog();

            }
        };
    }

    private Subscriber<AttributesVoucher> getSubscriberCheckVoucherCode() {
        return new Subscriber<AttributesVoucher>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideProgressDialog();
                    getView().onErrorCheckVoucherCode(e);
                }
            }

            @Override
            public void onNext(AttributesVoucher attributesVoucher) {
                getView().hideProgressDialog();
                getView().onSuccessCheckVoucherCode(attributesVoucher);
            }
        };
    }

    public Subscriber<Boolean> getSubscriberSubmitData() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorSubmitData(e);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessSubmitData();
            }
        };
    }

    @Override
    protected RequestParams getRequestParam() {
        RequestParams requestParams;
        if (getView().isRoundTrip()) {
            requestParams = addToCartUseCase.createRequestParam(
                    getView().getCurrentBookingReviewModel().getAdult(),
                    getView().getCurrentBookingReviewModel().getChildren(),
                    getView().getCurrentBookingReviewModel().getInfant(),
                    getView().getCurrentBookingReviewModel().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getReturnTripId(),
                    getView().getCurrentBookingReviewModel().getDepartureDate(),
                    getView().getCurrentBookingReviewModel().getReturnDate(),
                    getView().getIdEmpotencyKey(getView().getDepartureTripId() + "_" + getView().getReturnTripId())
            );
        } else {
            requestParams = addToCartUseCase.createRequestParam(
                    getView().getCurrentBookingReviewModel().getAdult(),
                    getView().getCurrentBookingReviewModel().getChildren(),
                    getView().getCurrentBookingReviewModel().getInfant(),
                    getView().getCurrentBookingReviewModel().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getCurrentBookingReviewModel().getDepartureDate(),
                    getView().getIdEmpotencyKey(getView().getDepartureTripId())
            );
        }
        return requestParams;
    }

    @Override
    protected BaseCartData getCurrentCartData() {
        return getView().getCurrentCartData();
    }

    @Override
    protected void updateTotalPrice(int totalPrice) {
        getView().setTotalPrice(totalPrice);
    }

    @Override
    protected void onCountDownTimestimeChanged(String timestamp) {
        getView().setTimeStamp(timestamp);
    }
}
