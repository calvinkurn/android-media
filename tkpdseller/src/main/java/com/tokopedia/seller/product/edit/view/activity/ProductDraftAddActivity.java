package com.tokopedia.seller.product.edit.view.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.fragment.ProductDraftAddFragment;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftAddActivity extends BaseProductAddEditActivity {

    public static final String PRODUCT_DRAFT_ID = "PRODUCT_DRAFT_ID";

    public static void start(Context context, Fragment fragment, long productDraftId) {
        Intent intent = createInstance(context, productDraftId);
        fragment.startActivityForResult(intent, ProductAddActivity.PRODUCT_REQUEST_CODE);
    }

    public static void start(Activity activity, long productDraftId) {
        Intent intent = createInstance(activity, productDraftId);
        activity.startActivityForResult(intent, ProductAddActivity.PRODUCT_REQUEST_CODE);
    }

    public static Intent createInstance(Context context, long productDraftId){
        Intent intent = new Intent(context, ProductDraftAddActivity.class);
        intent.putExtra(PRODUCT_DRAFT_ID, productDraftId);
        return intent;
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        String productId = getIntent().getStringExtra(PRODUCT_DRAFT_ID);
        if (TextUtils.isEmpty(productId)){
            Toast.makeText(this,getString(R.string.product_draft_error_cannot_load_draft),
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (savedInstance == null) {
            inflateFragment();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        long productDraftId = getIntent().getLongExtra(PRODUCT_DRAFT_ID, 0);
        return ProductDraftAddFragment.createInstance(productDraftId);
    }

    @Override
    protected int getCancelMessageRes() {
        return R.string.product_draft_dialog_cancel_message;
    }

    @Override
    protected boolean needDeleteCacheOnBack() {
        return false;
    }
}