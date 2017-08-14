package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewCostNewGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewProductListNewGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsCreatePromoNewGroupActivity extends BaseStepperActivity {
    List<Fragment> fragmentList;

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewProductListNewGroupFragment());
            fragmentList.add(new TopAdsNewCostNewGroupFragment());
            fragmentList.add(new TopAdsNewScheduleNewGroupFragment());
            return fragmentList;
        }else{
            return fragmentList;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String name = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            name = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_NAME);
        }
        stepperModel = new TopAdsCreatePromoNewGroupModel();
        ((TopAdsCreatePromoNewGroupModel)stepperModel).setGroupName(name);
        super.onCreate(savedInstanceState);
    }

    public static Intent createIntent(Context context, String name){
        Intent intent = new Intent(context, TopAdsCreatePromoNewGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, name);
        return intent;
    }
}
