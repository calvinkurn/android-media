package com.tokopedia.seller.product.edit.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddDescriptionPickerFragment;

/**
 * Created by nathan on 3/6/18.
 */

public class ProductAddDescriptionPickerActivity extends BaseSimpleActivity {

    public static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";

    private ProductAddDescriptionPickerFragment productAddDescriptionPickerFragment;

    public static void start(Activity activity, int requestCode, String description) {
        Intent intent = new Intent(activity, ProductAddDescriptionPickerActivity.class);
        intent.putExtra(PRODUCT_DESCRIPTION, description);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, int requestCode, String description) {
        Intent intent = new Intent(fragment.getActivity(), ProductAddDescriptionPickerActivity.class);
        intent.putExtra(PRODUCT_DESCRIPTION, description);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected Fragment getNewFragment() {
        productAddDescriptionPickerFragment = ProductAddDescriptionPickerFragment.newInstance(
                getIntent().getExtras().getString(PRODUCT_DESCRIPTION));
        return productAddDescriptionPickerFragment;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PRODUCT_DESCRIPTION, productAddDescriptionPickerFragment.getDescriptionText());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}
