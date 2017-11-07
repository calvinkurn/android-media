package com.tokopedia.ride.bookingride.view;

import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetPaymentMethodListCacheUseCase;
import com.tokopedia.ride.bookingride.domain.GetPaymentMethodListUseCase;
import com.tokopedia.ride.bookingride.domain.RequestApiUseCase;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethod;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Vishal on 25th Oct, 2017.
 */

public class ManagePaymentOptionsPresenter extends BaseDaggerPresenter<ManagePaymentOptionsContract.View>
        implements ManagePaymentOptionsContract.Presenter {

    private RequestApiUseCase saveUrlUseCase;
    private GetPaymentMethodListUseCase getPaymentMethodListUseCase;
    private GetPaymentMethodListCacheUseCase getPaymentMethodListCacheUseCase;
    private PaymentMethodList paymentMethodList;

    @Inject
    public ManagePaymentOptionsPresenter(GetPaymentMethodListUseCase getPaymentMethodListUseCase, GetPaymentMethodListCacheUseCase getPaymentMethodListCacheUseCase, RequestApiUseCase deleteCreditCardUseCase) {
        this.getPaymentMethodListUseCase = getPaymentMethodListUseCase;
        this.getPaymentMethodListCacheUseCase = getPaymentMethodListCacheUseCase;
        this.saveUrlUseCase = deleteCreditCardUseCase;
    }

    @Override
    public void fetchPaymentMethodList() {
        getPaymentMethodsFromCache();
        getPaymentMethodList();
    }

    private void getPaymentMethodList() {
        getView().showProgress();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetPaymentMethodListUseCase.PARAM_PAYMENT_METHOD, "cc");
        getPaymentMethodListUseCase.execute(requestParams, new Subscriber<PaymentMethodList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().hideProgress();
                getView().showErrorMessage(e.getMessage());
            }

            @Override
            public void onNext(PaymentMethodList paymentMethodList) {
                renderPaymentMethodList(paymentMethodList);
            }
        });
    }

    private void getPaymentMethodsFromCache() {
        getPaymentMethodListCacheUseCase.execute(RequestParams.EMPTY, new Subscriber<PaymentMethodList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(PaymentMethodList paymentMethodList) {
                renderPaymentMethodList(paymentMethodList);
            }
        });
    }

    private void renderPaymentMethodList(PaymentMethodList paymentMethodList) {
        ManagePaymentOptionsPresenter.this.paymentMethodList = paymentMethodList;
        List<Visitable> visitables = new ArrayList<>();
        getView().hideProgress();

        if (paymentMethodList != null && paymentMethodList.getPaymentMethods() != null) {

            for (PaymentMethod paymentMethod : paymentMethodList.getPaymentMethods()) {
                PaymentMethodViewModel visitable = new PaymentMethodViewModel();
                visitable.setActive(paymentMethod.getActive());
                visitable.setName(paymentMethod.getLabel());
                visitable.setType(paymentMethod.getMode());
                visitable.setImageUrl(paymentMethod.getCardTypeImage());
                visitable.setExpiryMonth(paymentMethod.getExpiryMonth());
                visitable.setExpiryYear(paymentMethod.getExpiryYear());
                visitable.setDeleteUrl(paymentMethod.getDeleteUrl());
                visitable.setDeleteBody(paymentMethod.getRemoveBody());
                visitable.setSaveurl(paymentMethod.getSaveUrl());
                visitable.setSaveBody(paymentMethod.getSaveBody());
                visitable.setCardType(paymentMethod.getCardType());

                visitables.add(visitable);
            }
        }

        getView().renderPaymentMethodList(visitables);
    }

    @Override
    public void addCreditCard() {
        if (paymentMethodList != null && paymentMethodList.getAddPayment() != null) {

            getView().opeScroogePage(paymentMethodList.getAddPayment().getSaveUrl(), true, paymentMethodList.getAddPayment().getSaveBody());
        }
    }

    @Override
    public void selectPaymentOption(final PaymentMethodViewModel paymentMethodViewModel) {
        getView().showProgressBar();
        saveUrlUseCase.setUrl(paymentMethodViewModel.getSaveurl());
        RequestParams params = RequestParams.create();

        //create params from bundle
        Bundle bundle = paymentMethodViewModel.getSaveBody();
        Set<String> set = bundle.keySet();
        for (String key : set) {
            params.putString(key, bundle.getString(key));
        }

        saveUrlUseCase.execute(params, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                if (isViewAttached()) {
                    getView().hideProgressBar();
                    String error;
                    if (e instanceof UnknownHostException) {
                        error = getView().getActivity().getString(R.string.error_no_connection);
                    } else if (e instanceof SocketTimeoutException) {
                        error = getView().getActivity().getString(R.string.error_timeout);
                    } else {
                        error = getView().getActivity().getString(R.string.error_default);
                    }
                    getView().showErrorMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached() && getView().getActivity() != null) {
                    getView().hideProgressBar();
                    getView().closeActivity(paymentMethodViewModel);
                }
            }
        });
    }
}
