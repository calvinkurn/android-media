package com.tokopedia.seller.product.manage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.manage.constant.ManageProductConstant;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.fragment.SortManageProductFragment;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class SortManageProductActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, @SortProductOption String sortProductOption){
        Intent intent = new Intent(context, SortManageProductActivity.class);
        intent.putExtra(ManageProductConstant.EXTRA_SORT_SELECTED, sortProductOption);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return SortManageProductFragment.createInstance(getIntent().getStringExtra(ManageProductConstant.EXTRA_SORT_SELECTED));
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
