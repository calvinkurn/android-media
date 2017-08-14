package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsEditProductMainPageFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsEditShopMainPageFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditShopMainPageActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, String shopId){
        Intent intent = new Intent(context, TopAdsEditShopMainPageActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, shopId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            String shopId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                shopId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsEditShopMainPageFragment.createInstance(shopId);
            return fragment;
        }
    }
}
