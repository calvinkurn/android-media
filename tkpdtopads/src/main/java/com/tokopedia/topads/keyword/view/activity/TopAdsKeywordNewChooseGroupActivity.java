package com.tokopedia.topads.keyword.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewChooseGroupFragment;

/**
 * Created by nathan on 5/17/17.
 */

public class TopAdsKeywordNewChooseGroupActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {

    public static final String TAG = TopAdsKeywordNewChooseGroupActivity.class.getSimpleName();

    private static final String EXTRA_IS_POSITIVE = "is_pos";

    public static void start(Activity activity, int requestCode,
                             boolean isPositive) {
        Intent intent = createIntent(activity, isPositive);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             boolean isPositive) {
        Intent intent = createIntent(context, isPositive);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static Intent createIntent(Context context, boolean isPositive) {
        Intent intent = new Intent(context, TopAdsKeywordNewChooseGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_IS_POSITIVE, isPositive);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        boolean isPositive = getIntent().getBooleanExtra(EXTRA_IS_POSITIVE, true);
        return TopAdsKeywordNewChooseGroupFragment.newInstance(isPositive);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}