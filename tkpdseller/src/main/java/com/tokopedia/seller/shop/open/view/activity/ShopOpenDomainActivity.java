package com.tokopedia.seller.shop.open.view.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenDomainFragment;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenDomainActivity extends BaseSimpleActivity implements HasComponent<ShopComponent> {

    public static void start(Activity activity){
        Intent intent = new Intent(activity, ShopOpenDomainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopOpenDomainFragment.newInstance();
    }

    @Override
    public ShopComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getShopComponent();
    }
}