package com.tokopedia.ride.bookingride.view;

import android.app.Activity;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by Vishal on 11/14/17.
 */

public interface PayPendingFareContract {
    interface View extends CustomerView {

        void showProgressbar();

        void hideProgressbar();

        void opeScroogePage(String url, String postData);

        Activity getActivity();

        void showErrorMessage(String error);
    }

    interface Presenter extends CustomerPresenter<View> {

        void onDestroy();

        void payPendingFare();
    }
}
