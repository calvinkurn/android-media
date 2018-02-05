package com.tokopedia.shop.info.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopInfoDetailFragment extends BaseDaggerFragment{

    public static ShopInfoDetailFragment createInstance() {
        ShopInfoDetailFragment shopIndoDetailFragment = new ShopInfoDetailFragment();
        Bundle bundle = new Bundle();
        return shopIndoDetailFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
