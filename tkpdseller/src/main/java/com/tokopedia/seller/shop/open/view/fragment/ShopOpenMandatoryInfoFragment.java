package com.tokopedia.seller.shop.open.view.fragment;

import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingInfoFragment;

/**
 * Created by nathan on 10/21/17.
 */

public class ShopOpenMandatoryInfoFragment extends ShopSettingInfoFragment {

    @Override
    public void onSuccessSaveInfoShop() {
        super.onSuccessSaveInfoShop();
        if (getActivity() instanceof StepperListener) {
            ((StepperListener) getActivity()).goToNextPage(null);
        }
    }
}
