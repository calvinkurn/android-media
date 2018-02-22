package com.tokopedia.shop.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListActivity extends BaseSimpleActivity implements HasComponent<ShopComponent> {

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "SHOP_DOMAIN";
    private String shopId;
    private String shopDomain;
    private ShopComponent component;

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(SHOP_ID, shopId);
        return intent;
    }

    public static Intent createIntentWithDomain(Context context, String shopDomain) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(SHOP_DOMAIN, shopDomain);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(SHOP_ID);
        shopDomain = getIntent().getStringExtra(SHOP_DOMAIN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopProductListFragment.createInstance(shopId);
    }

    @Override
    public ShopComponent getComponent() {
        if (component == null) {
            component = ShopComponentInstance.getComponent(getApplication());
        }
        return component;
    }
}
