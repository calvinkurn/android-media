package com.tokopedia.seller.product.view.activity;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductDraftEditFragment;

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
    protected void setupFragment() {
        inflateView(R.layout.activity_product_add);
        String productId = getIntent().getStringExtra(PRODUCT_DRAFT_ID);
        if (StringUtils.isBlank(productId)){
            throw new RuntimeException("Product id is not selected");
        }
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, ProductDraftEditFragment.createInstance(productId), ProductDraftEditFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected String getFragmentTAG() {
        return ProductDraftEditFragment.class.getSimpleName();
    }
}
