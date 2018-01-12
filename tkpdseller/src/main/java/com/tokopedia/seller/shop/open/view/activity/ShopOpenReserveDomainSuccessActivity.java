package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenCreateSuccessFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenReserveDomainSuccessFragment;


public class ShopOpenReserveDomainSuccessActivity extends BaseActivity implements HasComponent<ShopOpenDomainComponent>{

    public static final String EXTRA_SHOP_NAME = "shop_nm";

    public static Intent getIntent(Context context, String shopName) {
        Intent intent = new Intent(context, ShopOpenReserveDomainSuccessActivity.class);
        intent.putExtra(EXTRA_SHOP_NAME, shopName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_toolbar);
        if (savedInstanceState == null) {
            String shopName = getIntent().getStringExtra(EXTRA_SHOP_NAME);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ShopOpenReserveDomainSuccessFragment.newInstance(shopName),
                            ShopOpenCreateSuccessFragment.TAG)
                    .commit();
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
