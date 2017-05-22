package com.tokopedia.ride.bookingride.view;

import android.app.Activity;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 3/22/17.
 */

public interface ConfirmBookingContract {
    interface View extends CustomerView {
        RequestParams getParam();

        void renderFareEstimate(String fareId, String display, float value, float surgeMultipliers, String surgeConfirmationHref);

        void hideConfirmButton();

        Activity getActivity();

        float getFarePrice();

        void showTopupTokoCashButton();

        void hideTopupTokoCashButton();

        void showConfirmButton();

        void showMessage(String message);

        void setBalanceText(String balance);

        void showProgress();

        void hideProgress();

        void hideNotActivatedTokoCashLayout();

        void showNotActivatedTokoCashLayout(String redirectUrl);

        void showErrorMessage(String message);

        void hideErrorMessage();

        void goToProductList();

        void showConfirmLayout();

        void showPromoLayout();

        void hideConfirmLayout();

        void hidePromoLayout();

        void showToastMessage(String message);
    }

    interface Presenter extends CustomerPresenter<View> {

        void actionGetFareAndEstimate();

        void initialize();
    }
}
