package com.tokopedia.shop.info.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.info.di.component.ShopInfoComponent;
import com.tokopedia.shop.info.view.fragment.ShopInfoDetailFragment;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopInfoDetailActivity extends BaseSimpleActivity implements HasComponent<ShopInfoComponent> {

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopInfoDetailActivity.class);
        intent.putExtra(ShopParamConstant.SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopInfoDetailFragment.createInstance(getIntent().getStringExtra(ShopParamConstant.SHOP_ID));
    }

    @Override
    public ShopInfoComponent getComponent() {
        return ShopComponentInstance.getComponent(getApplication());
    }
}
