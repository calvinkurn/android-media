package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetPaymentMethodListUseCase;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethod;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;
import com.tokopedia.ride.common.ride.domain.model.ScroogeWebviewPostDataBody;
import com.tokopedia.ride.scrooge.ScroogePGUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Vishal on 25th Oct, 2017.
 */

public class ManagePaymentOptionsPresenter extends BaseDaggerPresenter<ManagePaymentOptionsContract.View>
        implements ManagePaymentOptionsContract.Presenter {

    private GetPaymentMethodListUseCase getPaymentMethodListUseCase;
    private PaymentMethodList paymentMethodList;

    @Inject
    public ManagePaymentOptionsPresenter(GetPaymentMethodListUseCase getPaymentMethodListUseCase) {
        this.getPaymentMethodListUseCase = getPaymentMethodListUseCase;
    }

    @Override
    public void renderPaymentMethodList() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetPaymentMethodListUseCase.PARAM_PAYMENT_METHOD, "cc");

        getView().showProgress();
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
                        visitable.setDeleteBody(paymentMethod.getRemoveBody() == null ? null : paymentMethod.getRemoveBody().getBundle());

                        visitables.add(visitable);
                    }
                }

                getView().renderPaymentMethodList(visitables);
            }
        });
    }

    @Override
    public void addCreditCard() {
        if (paymentMethodList != null && paymentMethodList.getAddPayment() != null) {
            String url = paymentMethodList.getAddPayment().getSaveUrl();
            ScroogeWebviewPostDataBody postBody = paymentMethodList.getAddPayment().getSaveBody();
            ScroogePGUtil.openScroogePage(getView().getActivity(), url, true, postBody.getBundle());
        }
    }
}
