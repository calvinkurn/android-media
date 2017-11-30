package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
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

public class FlightBookingReviewPresenter extends BaseDaggerPresenter<FlightBookingReviewContract.View> implements FlightBookingReviewContract.Presenter {

    private final FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase;
    private final FlightBookingReviewSubmitUseCase flightBookingReviewSubmitUseCase;
    private FlightAddToCartUseCase flightAddToCartUseCase;

    @Inject
    public FlightBookingReviewPresenter(FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase,
                                        FlightBookingReviewSubmitUseCase flightBookingReviewSubmitUseCase,
                                        FlightAddToCartUseCase flightAddToCartUseCase) {
        this.flightCheckVoucherCodeUseCase = flightCheckVoucherCodeUseCase;
        this.flightBookingReviewSubmitUseCase = flightBookingReviewSubmitUseCase;
        this.flightAddToCartUseCase = flightAddToCartUseCase;
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
    public void processGetCartData() {
        flightAddToCartUseCase.execute(RequestParams.create(), getSubscriberGetCartData());
    }

    public Subscriber<CartEntity> getSubscriberGetCartData() {
        return new Subscriber<CartEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideProgressDialog();
                    if (e instanceof FlightException &&
                            ((FlightException) e).getErrorList().contains(new FlightError(FlightErrorConstant.ADD_TO_CART))) {
                        getView().showExpireTransactionDialog();
                    } else {
                        getView().showUpdateDataErrorStateLayout(FlightErrorUtil.getMessageFromException(e));
                    }
                }
            }

            @Override
            public void onNext(CartEntity cartEntity) {
                getView().hideProgressDialog();
                getView().onGetCartData(cartEntity);
            }
        };
    }
}
