package com.tokopedia.tkpd.tkpdreputation.productreview.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, String productId){
        Intent intent = new Intent(context, ProductReviewActivity.class);
        intent.putExtra(ProductReviewFragment.EXTRA_PRODUCT_ID, productId);
        return intent;
    }


    @Override
    protected Fragment getNewFragment() {
        return ProductReviewFragment.getInstance(getIntent().getStringExtra(ProductReviewFragment.EXTRA_PRODUCT_ID));
    }
}
