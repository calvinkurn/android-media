package com.tokopedia.shop.etalase.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.etalase.view.fragment.ShopEtalaseFragment;
import com.tokopedia.shop.etalase.view.listener.ShopEtalaseFragmentListener;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseActivity extends BaseSimpleActivity implements HasComponent<ShopComponent>, ShopEtalaseFragmentListener {

    private static final String IS_GO_TO_SHOP_PRODUCT_LIST = "IS_GO_TO_SHOP_PRODUCT_LIST";

    private ShopComponent component;
    private String shopDomain;
    private String shopId;
    private boolean isGoToShopProductList;
    private String selectedEtalaseId;

    public static Intent createIntent(Context context, String shopId, String selectedEtalaseId, boolean isGoToShopProductList) {
        Intent intent = new Intent(context, ShopEtalaseActivity.class);
        intent.putExtra(ShopParamConstant.SHOP_ID, shopId);
        intent.putExtra(ShopParamConstant.SELECTED_ETALASE_ID, selectedEtalaseId);
        intent.putExtra(IS_GO_TO_SHOP_PRODUCT_LIST, isGoToShopProductList);
        return intent;
    }

    public static Intent createIntentWithDomain(Context context, String shopDomain, String selectedEtalaseId) {
        Intent intent = new Intent(context, ShopEtalaseActivity.class);
        intent.putExtra(ShopParamConstant.SHOP_DOMAIN, shopDomain);
        intent.putExtra(ShopParamConstant.SELECTED_ETALASE_ID, selectedEtalaseId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopEtalaseFragment.createInstance(shopId, shopDomain, selectedEtalaseId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopParamConstant.SHOP_ID);
        shopDomain = getIntent().getStringExtra(ShopParamConstant.SHOP_DOMAIN);
        selectedEtalaseId = getIntent().getStringExtra(ShopParamConstant.SELECTED_ETALASE_ID);
        isGoToShopProductList = getIntent().getBooleanExtra(IS_GO_TO_SHOP_PRODUCT_LIST, false);
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
        intent.putExtra(ShopParamConstant.ETALASE_ID, shopEtalaseViewModel.getEtalaseId());
        intent.putExtra(ShopParamConstant.ETALASE_NAME, shopEtalaseViewModel.getEtalaseName());
        setResult(Activity.RESULT_OK, intent);

        if (isGoToShopProductList) {
            startActivity(ShopProductListActivity.createIntent(this,
                    shopId,
                    null,
                    shopEtalaseViewModel.getEtalaseId(),
                    shopEtalaseViewModel.getEtalaseName()));
        }
        finish();
    }
}
