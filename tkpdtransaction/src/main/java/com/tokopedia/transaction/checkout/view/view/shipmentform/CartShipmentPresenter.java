package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.support.annotation.NonNull;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.transaction.checkout.domain.datamodel.toppay.ThanksTopPayData;
import com.tokopedia.transaction.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.transaction.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.transaction.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class CartShipmentPresenter implements ICartShipmentPresenter {

    private final CheckoutUseCase checkoutUseCase;
    private final CompositeSubscription compositeSubscription;
    private final GetThanksToppayUseCase getThanksToppayUseCase;
    private final CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase;
    private final ICartShipmentActivity cartShipmentActivity;

    @Inject
    public CartShipmentPresenter(CompositeSubscription compositeSubscription,
                                 CheckoutUseCase checkoutUseCase,
                                 GetThanksToppayUseCase getThanksToppayUseCase,
                                 CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase,
                                 ICartShipmentActivity cartShipmentActivity) {
        this.compositeSubscription = compositeSubscription;
        this.checkoutUseCase = checkoutUseCase;
        this.getThanksToppayUseCase = getThanksToppayUseCase;
        this.checkPromoCodeCartShipmentUseCase = checkPromoCodeCartShipmentUseCase;
        this.cartShipmentActivity = cartShipmentActivity;
    }

    @Override
    public void processCheckout(CheckoutRequest checkoutRequest) {
        cartShipmentActivity.showProgressLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckoutUseCase.PARAM_CARTS, checkoutRequest);
        compositeSubscription.add(
                checkoutUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberCheckoutCart())
        );
    }

    @Override
    public void processVerifyPayment(String transactionId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GetThanksToppayUseCase.PARAM_TRANSACTION_ID, transactionId);
        compositeSubscription.add(
                getThanksToppayUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(getSubscriberThanksTopPay())
        );
    }

    @Override
    public void checkPromoShipment(Subscriber<CheckPromoCodeCartShipmentResult> subscriber,
                                   CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CheckPromoCodeCartShipmentUseCase.PARAM_CARTS, checkPromoCodeCartShipmentRequest);
        compositeSubscription.add(
                checkPromoCodeCartShipmentUseCase.createObservable(requestParams)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @NonNull
    private Subscriber<ThanksTopPayData> getSubscriberThanksTopPay() {
        return new Subscriber<ThanksTopPayData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ThanksTopPayData thanksTopPayData) {
                cartShipmentActivity.renderThanksTopPaySuccess("Pembayaran Berhasil");
            }
        };
    }

    @NonNull
    private Subscriber<CheckoutData> getSubscriberCheckoutCart() {
        return new Subscriber<CheckoutData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cartShipmentActivity.hideProgressLoading();
                e.printStackTrace();

            }

            @Override
            public void onNext(CheckoutData checkoutData) {
                cartShipmentActivity.hideProgressLoading();
                if (!checkoutData.isError()) {
                    cartShipmentActivity.renderCheckoutCartSuccess(checkoutData);
                } else {
                    cartShipmentActivity.renderErrorCheckoutCart(checkoutData.getErrorMessage());
                }
            }
        };
    }
}
