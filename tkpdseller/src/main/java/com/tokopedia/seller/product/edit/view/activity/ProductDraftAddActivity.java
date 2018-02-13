package com.tokopedia.seller.product.edit.view.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.seller.product.edit.view.fragment.ProductDraftAddFragment;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftAddActivity extends ProductAddActivity {

    public static final String PRODUCT_DRAFT_ID = "PRODUCT_DRAFT_ID";

    public static void start(Context context, Fragment fragment, String productId) {
        Intent intent = createInstance(context, productId);
        fragment.startActivityForResult(intent, ProductAddActivity.PRODUCT_REQUEST_CODE);
    }

    public static void start(Activity activity, String productId) {
        Intent intent = createInstance(activity, productId);
        activity.startActivityForResult(intent, ProductAddActivity.PRODUCT_REQUEST_CODE);
    }

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductDraftAddActivity.class);
        intent.putExtra(PRODUCT_DRAFT_ID, productId);
        return intent;
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        String productId = getIntent().getStringExtra(PRODUCT_DRAFT_ID);
        if (TextUtils.isEmpty(productId)){
            throw new RuntimeException("Product id is not selected");
        }
        if (savedInstance == null) {
            inflateFragment();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        String productId = getIntent().getStringExtra(PRODUCT_DRAFT_ID);
        return ProductDraftAddFragment.createInstance(productId);
    }

    @Override
    protected boolean needDeleteCacheOnBack() {
        return false;
    }
}