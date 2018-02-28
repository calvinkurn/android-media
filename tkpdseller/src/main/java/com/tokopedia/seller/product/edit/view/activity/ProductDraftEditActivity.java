package com.tokopedia.seller.product.edit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.fragment.ProductDraftEditFragment;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDraftEditActivity extends ProductDraftAddActivity  {

    public static Intent createInstance(Context context, long productDraftId){
        Intent intent = new Intent(context, ProductDraftEditActivity.class);
        intent.putExtra(PRODUCT_DRAFT_ID, productDraftId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        long productId = getIntent().getLongExtra(PRODUCT_DRAFT_ID, Long.MIN_VALUE);
        return ProductDraftEditFragment.createInstance(productId);
    }

}