package com.tokopedia.seller.product.edit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.fragment.ProductDuplicateFragment;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDuplicateActivity extends BaseProductAddEditActivity {

    public static final String PRODUCT_ID = "PRODUCT_ID";

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductDuplicateActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        String productId = getIntent().getStringExtra(PRODUCT_ID);
        if (TextUtils.isEmpty(productId)){
            throw new RuntimeException("Product id is not selected");
        }
        if (savedInstance == null) {
            inflateFragment();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        String productId = getIntent().getStringExtra(PRODUCT_ID);
        return ProductDuplicateFragment.createInstance(productId);
    }

    @Override
    protected int getCancelMessageRes() {
        return R.string.product_draft_dialog_cancel_message;
    }

    @Override
    protected boolean needDeleteCacheOnBack() {
        return true;
    }
}