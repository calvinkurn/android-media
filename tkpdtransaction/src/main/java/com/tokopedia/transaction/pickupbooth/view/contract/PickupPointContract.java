package com.tokopedia.transaction.pickupbooth.view.contract;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.transaction.pickupbooth.view.model.StoreViewModel;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public interface PickupPointContract {

    interface View extends CustomerView {
        Activity getActivity();

        void showLoading();

        void hideLoading();

        void showResult();

        void showNoResult();

        void showNoConnection(@NonNull String message);
    }

    interface Presenter extends CustomerPresenter<View> {
        void queryPickupPoints(String keyword);

        ArrayList<StoreViewModel> getPickupPoints();
    }

    interface Constant {
        String INTENT_DATA_PARAMS = "params";
    }
}
