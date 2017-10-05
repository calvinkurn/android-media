package com.tokopedia.seller.product.manage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageFilterFragment;
import com.tokopedia.seller.product.manage.view.model.ProductManageFilterModel;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageFilterActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, ProductManageFilterModel productManageFilterModel){
        Intent intent = new Intent(context, ProductManageFilterActivity.class);
        intent.putExtra(ProductManageConstant.EXTRA_FILTER_SELECTED, productManageFilterModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        ProductManageFilterModel productManageFilterModel = getIntent().getParcelableExtra(ProductManageConstant.EXTRA_FILTER_SELECTED);
        return ProductManageFilterFragment.createInstance(productManageFilterModel);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
