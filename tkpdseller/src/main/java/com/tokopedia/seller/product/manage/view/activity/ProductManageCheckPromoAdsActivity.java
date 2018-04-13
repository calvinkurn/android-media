package com.tokopedia.seller.product.manage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageCheckPromoAdsFragment;

/**
 * Created by hadi.putra on 12/04/18.
 */

public class ProductManageCheckPromoAdsActivity extends BaseSimpleActivity implements HasComponent<ProductComponent> {

    public static final String EXTRA_PARAM_SHOP_ID = "shop_id";
    public static final String EXTRA_PARAM_ITEM_ID = "item_id";

    private String shopId;
    private String itemId;

    public static Intent createIntent(Context context, String shopId, String itemId){
        Intent intent = new Intent(context, ProductManageCheckPromoAdsActivity.class);
        intent.putExtra(EXTRA_PARAM_SHOP_ID, shopId);
        intent.putExtra(EXTRA_PARAM_ITEM_ID, itemId);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(EXTRA_PARAM_SHOP_ID);
        itemId = getIntent().getStringExtra(EXTRA_PARAM_ITEM_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductManageCheckPromoAdsFragment.createInstance(shopId, itemId);
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }
}
