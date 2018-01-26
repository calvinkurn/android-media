package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.view.presenter.FlightBaseBookingPresenter;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.FlightBookingCheckoutUseCase;
import com.tokopedia.flight.review.domain.FlightBookingVerifyUseCase;
import com.tokopedia.flight.review.domain.FlightCheckVoucherCodeUseCase;
import com.tokopedia.flight.review.domain.verifybooking.model.response.CartItem;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPresenter extends FlightBaseBookingPresenter<FlightBookingReviewContract.View> implements FlightBookingReviewContract.Presenter {

    private final FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase;
    private final FlightBookingCheckoutUseCase flightBookingCheckoutUseCase;
    private final FlightBookingVerifyUseCase flightBookingVerifyUseCase;
    private FlightAnalytics flightAnalytics;

    @Inject
    public FlightBookingReviewPresenter(FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase,
                                        FlightBookingCheckoutUseCase flightBookingCheckoutUseCase,
                                        FlightAddToCartUseCase flightAddToCartUseCase,
                                        FlightBookingCartDataMapper flightBookingCartDataMapper,
                                        FlightBookingVerifyUseCase flightBookingVerifyUseCase,
                                        FlightAnalytics flightAnalytics) {
        super(flightAddToCartUseCase, flightBookingCartDataMapper);
        this.flightCheckVoucherCodeUseCase = flightCheckVoucherCodeUseCase;
        this.flightBookingCheckoutUseCase = flightBookingCheckoutUseCase;
        this.flightBookingVerifyUseCase = flightBookingVerifyUseCase;
        this.flightAnalytics = flightAnalytics;
    }

    @Override
    public void verifyBooking(String promoCode, int price, int adult, String cartId,
                              List<FlightBookingPassengerViewModel> flightPassengerViewModels,
                              String contactName, String country, String email, String phone) {
        getView().showCheckoutLoading();
        flightBookingVerifyUseCase.createObservable(
                flightBookingVerifyUseCase.createRequestParams(
                        promoCode,
                        price,
                        cartId,
                        flightPassengerViewModels,
                        contactName,
                        country,
                        email,
                        phone
                )
        ).flatMap(new Func1<DataResponseVerify, Observable<FlightCheckoutEntity>>() {
            @Override
            public Observable<FlightCheckoutEntity> call(DataResponseVerify dataResponseVerify) {
                if (dataResponseVerify.getAttributesData().getCartItems() != null && dataResponseVerify.getAttributesData().getCartItems().size() > 0) {
                    CartItem verifyCartItem = dataResponseVerify.getAttributesData().getCartItems().get(0);
                    int totalPrice = verifyCartItem.getConfiguration().getPrice();
                    String flightId = verifyCartItem.getMetaData().getInvoiceId();
                    String cartId = verifyCartItem.getMetaData().getCartId();
                    RequestParams requestParams;
                    if (dataResponseVerify.getAttributesData().getPromo() != null && dataResponseVerify.getAttributesData().getPromo().getCode().length() > 0) {
                        requestParams = flightBookingCheckoutUseCase.createRequestParam(cartId, flightId, totalPrice, dataResponseVerify.getAttributesData().getPromo().getCode());
                    } else {
                        requestParams = flightBookingCheckoutUseCase.createRequestParam(cartId, flightId, totalPrice);
                    }
                    return flightBookingCheckoutUseCase.createObservable(requestParams);
                }
                throw new RuntimeException("Failed to checkout");
            }
        }).map(new Func1<FlightCheckoutEntity, FlightCheckoutViewModel>() {
            @Override
            public FlightCheckoutViewModel call(FlightCheckoutEntity checkoutEntity) {
                FlightCheckoutViewModel viewModel = new FlightCheckoutViewModel();
                viewModel.setPaymentId(checkoutEntity.getAttributes().getParameter().getTransactionId());
                viewModel.setTransactionId(checkoutEntity.getAttributes().getParameter().getTransactionId());
                viewModel.setQueryString(checkoutEntity.getAttributes().getQueryString());
                viewModel.setRedirectUrl(checkoutEntity.getAttributes().getRedirectUrl());
                viewModel.setCallbackSuccessUrl(checkoutEntity.getAttributes().getCallbackUrlSuccess());
                viewModel.setCallbackFailedUrl(checkoutEntity.getAttributes().getCallbackUrlFailed());
                return viewModel;
            }
        })
                .onBackpressureDrop()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FlightCheckoutViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().setNeedToRefreshOnPassengerInfo();
                            getView().hideCheckoutLoading();
                            if (e instanceof FlightException) {
                                getView().showErrorInSnackbar(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
                            } else {
                                getView().showErrorInEmptyState(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
                            }
                            flightAnalytics.eventFailedPurchaseAttempt();
                        }
                    }

                    @Override
                    public void onNext(FlightCheckoutViewModel flightCheckoutViewModel) {
                        flightAnalytics.eventPurchaseAttempt(flightCheckoutViewModel);
                        getView().setNeedToRefreshOnPassengerInfo();
                        getView().navigateToTopPay(flightCheckoutViewModel);
                    }
                });
    }

    @Override
    public void checkVoucherCode(String cartId, String voucherCode) {
        getView().showProgressDialog();
        flightAnalytics.eventVoucherClick(voucherCode);
        flightCheckVoucherCodeUseCase.execute(flightCheckVoucherCodeUseCase.createRequestParams(cartId, voucherCode), getSubscriberCheckVoucherCode());
    }

    @Override
    public void submitData() {
        flightBookingCheckoutUseCase.execute(RequestParams.create(), getSubscriberSubmitData());
    }

    @Override
    public void onPaymentSuccess() {
        getView().navigateToOrderList();
    }

    @Override
    public void onPaymentFailed() {
        getView().showPaymentFailedErrorMessage(R.string.flight_review_failed_checkout_message);
    }

    @Override
    public void onPaymentCancelled() {
        getView().setNeedToRefreshOnPassengerInfo();
        getView().showPaymentFailedErrorMessage(R.string.flight_review_cancel_checkout_message);
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
                // TODO integrate with checkout
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
                    flightAnalytics.eventVoucherErrors(getView().getVoucherCode(), e.getMessage());
                }
            }

            @Override
            public void onNext(AttributesVoucher attributesVoucher) {
                getView().hideProgressDialog();
                flightAnalytics.eventVoucherSuccess(attributesVoucher.getVoucherCode(), attributesVoucher.getMessage());
                getView().onSuccessCheckVoucherCode(attributesVoucher);
                getView().updateFinalTotal(attributesVoucher, getView().getCurrentBookingReviewModel());
            }
        };
    }

    public Subscriber<FlightCheckoutEntity> getSubscriberSubmitData() {
        return new Subscriber<FlightCheckoutEntity>() {
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
            public void onNext(FlightCheckoutEntity checkoutEntity) {
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
                    getView().getIdEmpotencyKey(getView().getDepartureTripId() + "_" + getView().getReturnTripId())
            );
        } else {
            requestParams = addToCartUseCase.createRequestParam(
                    getView().getCurrentBookingReviewModel().getAdult(),
                    getView().getCurrentBookingReviewModel().getChildren(),
                    getView().getCurrentBookingReviewModel().getInfant(),
                    getView().getCurrentBookingReviewModel().getFlightClass().getId(),
                    getView().getDepartureTripId(),
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
