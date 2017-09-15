package com.tokopedia.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostNewGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListNewGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleShopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoShopActivity extends BaseStepperActivity {
    List<Fragment> fragmentList;

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewCostShopFragment());
            fragmentList.add(new TopAdsNewScheduleShopFragment());
            return fragmentList;
        }else{
            return fragmentList;
        }
    }

    public static Intent createIntent(Context context,String name) {
        Intent intent = new Intent(context, TopAdsCreatePromoShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, name);
        return intent;
    }

    @Override
    public void finishPage() {
        setResultAdSaved();
        super.finishPage();
    }

    private void setResultAdSaved() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        setResult(Activity.RESULT_OK, intent);
    }
}
