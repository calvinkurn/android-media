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

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";

    public static void start(Context context, Fragment fragment, long draftProductId) {
        Intent intent = createInstance(context, draftProductId);
        fragment.startActivityForResult(intent, ProductAddActivity.PRODUCT_REQUEST_CODE);
    }

    public static void start(Activity activity, long draftProductId) {
        Intent intent = createInstance(activity, draftProductId);
        activity.startActivityForResult(intent, ProductAddActivity.PRODUCT_REQUEST_CODE);
    }

    public static Intent createInstance(Context context, long draftProductId){
        Intent intent = new Intent(context, ProductDraftAddActivity.class);
        intent.putExtra(DRAFT_PRODUCT_ID, draftProductId);
        return intent;
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        long draftProductId = getIntent().getLongExtra(DRAFT_PRODUCT_ID, Long.MIN_VALUE);
        if (draftProductId < 0){
            Toast.makeText(this,getString(R.string.product_draft_error_cannot_load_draft), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (savedInstance == null) {
            inflateFragment();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        long draftProductId = getIntent().getLongExtra(DRAFT_PRODUCT_ID, Long.MIN_VALUE);
        return ProductDraftAddFragment.createInstance(draftProductId);
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