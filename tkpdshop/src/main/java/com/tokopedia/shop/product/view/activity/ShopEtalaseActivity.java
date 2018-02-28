package com.tokopedia.shop.product.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.view.fragment.ShopEtalaseFragment;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment;
import com.tokopedia.shop.product.view.listener.ShopEtalaseFragmentListener;
import com.tokopedia.shop.product.view.model.ShopEtalaseViewModel;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseActivity extends BaseSimpleActivity implements HasComponent<ShopComponent>, ShopEtalaseFragmentListener {
    private ShopComponent component;
    private String shopDomain;
    private String shopId;

    public static final String SELECTED_ETALASE_ID = "SELECTED_ETALASE_ID";
    private String selectedEtalaseId;

    @Override
    protected Fragment getNewFragment() {
        return ShopEtalaseFragment.createInstance(shopId, shopDomain, selectedEtalaseId);
    }

    public static Intent createIntent(Context context, String shopId, String selectedEtalaseId) {
        Intent intent = new Intent(context, ShopEtalaseActivity.class);
        intent.putExtra(ShopProductListActivity.SHOP_ID, shopId);
        intent.putExtra(SELECTED_ETALASE_ID, selectedEtalaseId);
        return intent;
    }

    public static Intent createIntentWithDomain(Context context, String shopDomain, String selectedEtalaseId) {
        Intent intent = new Intent(context, ShopEtalaseActivity.class);
        intent.putExtra(ShopProductListActivity.SHOP_DOMAIN, shopDomain);
        intent.putExtra(SELECTED_ETALASE_ID, selectedEtalaseId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopProductListActivity.SHOP_ID);
        shopDomain = getIntent().getStringExtra(ShopProductListActivity.SHOP_DOMAIN);
        selectedEtalaseId = getIntent().getStringExtra(SELECTED_ETALASE_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    public ShopComponent getComponent() {
        if (component == null) {
            component = ShopComponentInstance.getComponent(getApplication());
        }
        return component;
    }

    @Override
    public void select(ShopEtalaseViewModel shopEtalaseViewModel) {
        Intent intent = new Intent();
        intent.putExtra(ShopProductListFragment.ETALASE_ID, shopEtalaseViewModel.getEtalaseId());
        intent.putExtra(ShopProductListFragment.ETALASE_NAME, shopEtalaseViewModel.getEtalaseName());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
