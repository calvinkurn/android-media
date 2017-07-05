package com.tokopedia.seller.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductDuplicateFragment;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDuplicateActivity extends ProductDraftAddActivity {

    public static final String PRODUCT_ID = "PRODUCT_ID";

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductDuplicateActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void setupFragment() {
        String productId = getIntent().getStringExtra(PRODUCT_ID);
        if (StringUtils.isBlank(productId)){
            throw new RuntimeException("Product id is not selected");
        }
        inflateFragment(productId);
    }

    private void inflateFragment(String productId) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getFragmentTAG());
        if (fragment == null) {
            fragment = ProductDuplicateFragment.createInstance(productId);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, getFragmentTAG());
        fragmentTransaction.commit();
    }

    @Override
    protected String getFragmentTAG() {
        return ProductDuplicateFragment.class.getSimpleName();
    }
}
