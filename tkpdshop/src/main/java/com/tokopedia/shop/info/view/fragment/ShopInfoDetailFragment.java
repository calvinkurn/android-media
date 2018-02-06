package com.tokopedia.shop.info.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.shop.common.constant.ShopParamConstant;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopInfoDetailFragment extends BaseDaggerFragment{

    public static ShopInfoDetailFragment createInstance(String shopId) {
        ShopInfoDetailFragment shopIndoDetailFragment = new ShopInfoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_ID, shopId);
        shopIndoDetailFragment.setArguments(bundle);
        return shopIndoDetailFragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
