package com.tokopedia.shop.info.view.activity;

import android.support.v4.view.PagerAdapter;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;

/**
 * Created by nathan on 2/3/18.
 */

public class ShopInfoActivity extends BaseTabActivity {
    
    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return null;
    }

    @Override
    protected int getPageLimit() {
        return 0;
    }
}
