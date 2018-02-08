package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenRoutingFragment;

/**
 * Created by nathan on 12/19/17.
 */

public class ShopOpenRoutingActivity extends BaseSimpleActivity implements HasComponent<ShopOpenDomainComponent> {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ShopOpenRoutingActivity.class);
        return intent;
    }

    @DeepLink(Constants.Applinks.CREATE_SHOP)
    public static Intent getCallingApplinkCreateShopIntent(Context context, Bundle extras) {
        if (SessionHandler.isV4Login(context) && !SessionHandler.isUserHasShop(context)) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            return ShopOpenRoutingActivity.getIntent(context).setData(uri.build());
        } else {
            return HomeRouter.getHomeActivityInterfaceRouter(context);
        }
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopOpenRoutingFragment.newInstance();
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