package com.tokopedia.seller.shop.open.view.fragment;

import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;

/**
 * Created by nathan on 10/21/17.
 */

public class ShopOpenMandatoryLocationFragment extends ShopSettingLocationFragment {

    @Override
    protected void onNextButtonClicked() {
        if (getActivity() instanceof StepperListener) {
            ((StepperListener) getActivity()).goToNextPage(null);
        }
    }
}
