package com.tokopedia.seller.product.manage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSortFragment;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageSortActivity extends BaseSimpleActivity implements HasComponent<ProductComponent> {

    public static Intent createIntent(Context context, @SortProductOption String sortProductOption){
        Intent intent = new Intent(context, ProductManageSortActivity.class);
        intent.putExtra(ProductManageConstant.EXTRA_SORT_SELECTED, sortProductOption);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductManageSortFragment.createInstance(getIntent().getStringExtra(ProductManageConstant.EXTRA_SORT_SELECTED));
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void setToolbarColorWhite() {
        super.setToolbarColorWhite();
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close));
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter)getApplication()).getProductComponent();
    }
}
