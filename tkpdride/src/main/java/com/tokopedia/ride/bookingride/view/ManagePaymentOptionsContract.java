package com.tokopedia.ride.bookingride.view;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * Created by alvarisi on 4/25/17.
 */

public interface ManagePaymentOptionsContract {
    interface View extends CustomerView {
        Activity getActivity();

        void showErrorMessage(String message);

        void renderPaymentMethodList(List<Visitable> visitables);

        void hideProgress();

        void showProgress();
    }

    interface Presenter extends CustomerPresenter<View> {
        void renderPaymentMethodList();

        void addCreditCard();
    }
}
