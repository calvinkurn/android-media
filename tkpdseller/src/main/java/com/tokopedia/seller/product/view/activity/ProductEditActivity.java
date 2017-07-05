package com.tokopedia.seller.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductEditFragment;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditActivity extends ProductDraftEditActivity {

    public static final String PRODUCT_ID = "PRODUCT_ID";

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductEditActivity.class);
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
            fragment = ProductEditFragment.createInstance(productId);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, getFragmentTAG());
        fragmentTransaction.commit();
    }

    @Override
    protected String getFragmentTAG() {
        return ProductEditFragment.class.getSimpleName();
    }

    protected int getCancelMessageRes(){
        return R.string.product_draft_dialog_edit_cancel_message;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_EDIT_PRODUCT;
    }
}
