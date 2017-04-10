package com.tokopedia.ride.bookingride.view;

import android.app.Activity;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 3/22/17.
 */

public interface ConfirmBookingContract {
    interface View extends CustomerView{
        RequestParams getParam();

        void showErrorChangeSeat(String message);

        void renderFareEstimate(String fareId, String display, float value);

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
    }

    interface Presenter extends CustomerPresenter<View>{

        void actionCheckBalance();

        void actionChangeSeatCount();

        void initialize();

        void clearTokoCashCache();
    }
}
