package com.tokopedia.ride.bookingride.view;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;

import java.util.List;

/**
 * Created by Vishal on  2nd Nov, 2017.
 */

public interface ManagePaymentOptionsContract {
    interface View extends CustomerView {
        Activity getActivity();

        void showErrorMessage(String message);

        void renderPaymentMethodList(List<Visitable> visitables);

        void hideProgress();

        void showProgress();

        void showProgressBar();

        void hideProgressBar();

        void closeActivity(PaymentMethodViewModel paymentMethodViewModel);

        void opeScroogePage(String saveUrl, boolean isPostReq, Bundle saveBody);

        void showAutoDebitDialog(PaymentMethodViewModel paymentMethodViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void fetchPaymentMethodList();

        void getPaymentMethodsFromCloud();

        void addCreditCard();

        void selectPaymentOption(PaymentMethodViewModel paymentMethodViewModel);

        void deletePaymentMethodCache();
    }
}
