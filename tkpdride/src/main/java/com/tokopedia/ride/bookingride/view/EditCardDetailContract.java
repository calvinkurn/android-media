package com.tokopedia.ride.bookingride.view;

import android.app.Activity;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;

/**
 * Created by Vishal on 2nd Nov, 2017.
 */

public interface EditCardDetailContract {
    interface View extends CustomerView {
        Activity getActivity();

        void showProgress();

        void hideProgress();

        void showErrorMessage(String message);

        void closeActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
        void deleteCard(PaymentMethodViewModel paymentMethodViewModel);
    }
}
