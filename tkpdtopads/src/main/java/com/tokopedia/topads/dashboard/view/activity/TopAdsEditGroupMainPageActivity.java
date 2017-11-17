package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditGroupMainPageFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditGroupMainPageActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, GroupAd groupAd, String adId, boolean isForceRefresh){
        Intent intent = new Intent(context, TopAdsEditGroupMainPageActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, groupAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FORCE_REFRESH, isForceRefresh);
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
            GroupAd ad = null;
            String adId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
                adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsEditGroupMainPageFragment.createInstance(ad, adId);
            return fragment;
        }
    }
}
