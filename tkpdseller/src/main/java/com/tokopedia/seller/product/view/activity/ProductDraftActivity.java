package com.tokopedia.seller.product.view.activity;


import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductDraftFragment;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftActivity extends ProductAddActivity {

    public static final String PRODUCT_DRAFT_ID = "PRODUCT_DRAFT_ID";

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductDraftActivity.class);
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
                .add(R.id.container, ProductDraftFragment.createInstance(productId), ProductDraftFragment.class.getSimpleName())
                .commit();
    }

}
