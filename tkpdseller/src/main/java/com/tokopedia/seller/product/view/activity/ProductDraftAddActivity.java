package com.tokopedia.seller.product.view.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.CategoryPickerFragment;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.fragment.ProductDraftAddFragment;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftAddActivity extends ProductAddActivity {

    public static final String PRODUCT_DRAFT_ID = "PRODUCT_DRAFT_ID";

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductDraftAddActivity.class);
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

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProductDraftAddFragment.TAG);
        if (fragment == null) {
            fragment = ProductDraftAddFragment.createInstance(productId);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, ProductDraftAddFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected String getFragmentTAG() {
        return ProductDraftAddFragment.class.getSimpleName();
    }
}
