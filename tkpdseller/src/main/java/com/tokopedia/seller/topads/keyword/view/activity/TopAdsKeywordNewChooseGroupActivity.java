package com.tokopedia.seller.topads.keyword.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordNewChooseGroupFragment;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailEditShopFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewGroupFragment;

import java.util.ArrayList;

/**
 * Created by nathan on 5/17/17.
 */

public class TopAdsKeywordNewChooseGroupActivity extends TopAdsBaseSimpleActivity implements HasComponent<AppComponent> {

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
    protected Fragment getNewFragment(Bundle savedinstancestate) {
        boolean isPositive = getIntent().getBooleanExtra(EXTRA_IS_POSITIVE, true);
        return TopAdsKeywordNewChooseGroupFragment.newInstance(isPositive);
    }

    @Override
    protected String getTagFragment() {
        return TopAdsKeywordNewChooseGroupActivity.class.getSimpleName();
    }
}