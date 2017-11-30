package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.view.presenter.FlightBaseBookingPresenter;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.common.constant.FlightErrorConstant;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.domain.FlightBookingReviewSubmitUseCase;
import com.tokopedia.flight.review.domain.FlightCheckVoucherCodeUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPresenter extends FlightBaseBookingPresenter<FlightBookingReviewContract.View> implements FlightBookingReviewContract.Presenter {

    private final FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase;
    private final FlightBookingReviewSubmitUseCase flightBookingReviewSubmitUseCase;

    @Inject
    public FlightBookingReviewPresenter(FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase,
                                        FlightBookingReviewSubmitUseCase flightBookingReviewSubmitUseCase,
                                        FlightAddToCartUseCase flightAddToCartUseCase,
                                        FlightBookingCartDataMapper flightBookingCartDataMapper) {
        super(flightAddToCartUseCase, flightBookingCartDataMapper);
        this.flightCheckVoucherCodeUseCase = flightCheckVoucherCodeUseCase;
        this.flightBookingReviewSubmitUseCase = flightBookingReviewSubmitUseCase;
    }

    @Override
    public void checkVoucherCode(String cartId, String voucherCode) {
        getView().showProgressDialog();
        flightCheckVoucherCodeUseCase.execute(flightCheckVoucherCodeUseCase.createRequestParams(cartId, voucherCode), getSubscriberCheckVoucherCode());
    }

    @Override
    public void submitData() {
        flightBookingReviewSubmitUseCase.execute(RequestParams.create(), getSubscriberSubmitData());
    }

    private Subscriber<AttributesVoucher> getSubscriberCheckVoucherCode() {
        return new Subscriber<AttributesVoucher>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().hideProgressDialog();
                    getView().onErrorCheckVoucherCode(FlightErrorUtil.getMessageFromException(e));
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
                if(isViewAttached()){
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
