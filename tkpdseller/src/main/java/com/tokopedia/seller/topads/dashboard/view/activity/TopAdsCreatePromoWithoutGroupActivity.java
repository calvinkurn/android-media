package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewCostWithoutGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewProductListWithoutGroupFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoWithoutGroupActivity extends BaseStepperActivity {
    List<Fragment> fragmentList;

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewProductListWithoutGroupFragment());
            fragmentList.add(new TopAdsNewCostWithoutGroupFragment());
            return fragmentList;
        }else{
            return fragmentList;
        }
    }

    public static Intent createIntent(Context context){
        Intent intent = new Intent(context, TopAdsCreatePromoWithoutGroupActivity.class);
        return intent;
    }
}
