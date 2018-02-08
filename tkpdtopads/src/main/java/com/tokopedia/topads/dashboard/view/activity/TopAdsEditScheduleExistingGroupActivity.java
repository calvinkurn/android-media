package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditCostExistingGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditScheduleExistingGroupFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditScheduleExistingGroupActivity extends BaseSimpleActivity implements HasComponent<AppComponent> {
    public static Intent createIntent(Context context, String groupId){
        Intent intent = new Intent(context, TopAdsEditScheduleExistingGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        return intent;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            String groupId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                groupId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsEditScheduleExistingGroupFragment.createInstance(groupId);
            return fragment;
        }
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
