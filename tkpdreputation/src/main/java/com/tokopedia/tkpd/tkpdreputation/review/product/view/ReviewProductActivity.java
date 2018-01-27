package com.tokopedia.tkpd.tkpdreputation.review.product.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ReviewProductActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    public static Intent createIntent(Context context, String productId) {
        Intent intent = new Intent(context, ReviewProductActivity.class);
        intent.putExtra(ReviewProductFragment.EXTRA_PRODUCT_ID, productId);
        return intent;
    }


    @Override
    protected Fragment getNewFragment() {
        return ReviewProductFragment.getInstance(getIntent().getExtras().getString(ReviewProductFragment.EXTRA_PRODUCT_ID));
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }
}
