package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditCostShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditScheduleExistingGroupFragment;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsEditCostShopActivity extends BaseSimpleActivity implements HasComponent<TopAdsComponent> {
    public static Intent createIntent(Context context, String shopId){
        Intent intent = new Intent(context, TopAdsEditCostShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, shopId);
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
            String shopId = null;
            if (getIntent() != null && getIntent().getExtras() != null) {
                shopId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            }
            fragment = TopAdsEditCostShopFragment.createInstance(shopId);
            return fragment;
        }
    }

    @Override
    public TopAdsComponent getComponent() {
        if(getApplication() instanceof TopAdsModuleRouter){
            return ((TopAdsModuleRouter)getApplication()).getTopAdsComponent();
        }else{
            return null;
        }
    }
}
