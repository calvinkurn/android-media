package com.tokopedia.tkpd.tkpdreputation.productreview.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    public static Intent createIntent(Context context, String productId) {
        Intent intent = new Intent(context, ProductReviewActivity.class);
        intent.putExtra(ProductReviewFragment.EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductReviewFragment.getInstance(getIntent().getExtras().getString(ProductReviewFragment.EXTRA_PRODUCT_ID));
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }
}
