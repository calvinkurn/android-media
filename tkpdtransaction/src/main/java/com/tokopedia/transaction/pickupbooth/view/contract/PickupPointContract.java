package com.tokopedia.transaction.pickupbooth.view.contract;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public interface PickupPointContract {

    interface View extends CustomerView {
        Activity getActivity();

        void showLoading();

        void hideLoading();

        void showNoConnection(@NonNull String message);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getPickupPoints(String keyword);
    }

    interface Constant {
        String INTENT_DATA_PARAMS = "params";
    }
}
