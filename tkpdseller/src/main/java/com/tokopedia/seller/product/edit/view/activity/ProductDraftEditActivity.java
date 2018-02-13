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

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductDraftEditActivity.class);
        intent.putExtra(PRODUCT_DRAFT_ID, productId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        String productId = getIntent().getStringExtra(PRODUCT_DRAFT_ID);
        return ProductDraftEditFragment.createInstance(productId);
    }

}