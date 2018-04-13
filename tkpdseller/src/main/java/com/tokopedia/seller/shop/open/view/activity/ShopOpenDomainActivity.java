package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenReserveDomainFragment;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenDomainActivity extends BaseSimpleActivity
        implements HasComponent<ShopOpenDomainComponent> {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ShopOpenDomainActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {

        if (getIntent() != null) {
            boolean isFromAppShortCut = getIntent().getBooleanExtra(Constants.FROM_APP_SHORTCUTS, false);
            return ShopOpenReserveDomainFragment.newInstance(isFromAppShortCut);
        } else {
            return ShopOpenReserveDomainFragment.newInstance();
        }
    }

    @Override
    public ShopOpenDomainComponent getComponent() {
        return DaggerShopOpenDomainComponent
                .builder()
                .shopOpenDomainModule(new ShopOpenDomainModule())
                .shopComponent(((SellerModuleRouter) getApplication()).getShopComponent())
                .build();
    }
}