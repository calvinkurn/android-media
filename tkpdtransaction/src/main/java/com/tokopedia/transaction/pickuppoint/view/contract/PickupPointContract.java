package com.tokopedia.transaction.pickuppoint.view.contract;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.transaction.pickuppoint.view.model.StoreViewModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public interface PickupPointContract {

    interface View extends CustomerView {
        Activity getActivity();

        void showLoading();

        void hideLoading();

        void showAllResult();

        void showSearchResult(ArrayList<StoreViewModel> storeViewModels);

        void showNoResult();

        void showNoConnection(@NonNull String message);
    }

    interface Presenter extends CustomerPresenter<View> {
        void queryPickupPoints(String keyword, HashMap<String, String> param);

        ArrayList<StoreViewModel> getPickupPoints();
    }

    interface Constant {
        String INTENT_REQ_PARAMS = "params";

        String INTENT_DISTRICT_NAME = "district_name";

        String INTENT_DATA_STORE = "store";

        String INTENT_CART_ITEM = "cart_item";

        String INTENT_DATA_POSITION = "position";

    }
}