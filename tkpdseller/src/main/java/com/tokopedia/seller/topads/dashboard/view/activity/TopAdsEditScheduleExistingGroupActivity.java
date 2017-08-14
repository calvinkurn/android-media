package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsEditCostExistingGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsEditScheduleExistingGroupFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditScheduleExistingGroupActivity extends BaseSimpleActivity {
    public static Intent createIntent(Context context, String groupId){
        Intent intent = new Intent(context, TopAdsEditScheduleExistingGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        return intent;
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
}
