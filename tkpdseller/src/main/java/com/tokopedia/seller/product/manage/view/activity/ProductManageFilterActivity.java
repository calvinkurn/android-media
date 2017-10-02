package com.tokopedia.seller.product.manage.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageFilterFragment;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageFilterActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return new ProductManageFilterFragment();
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
}
