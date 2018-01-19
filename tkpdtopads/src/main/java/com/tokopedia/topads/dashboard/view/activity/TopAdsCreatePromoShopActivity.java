package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleShopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoShopActivity extends BaseStepperActivity implements HasComponent<AppComponent> {
    private List<Fragment> fragmentList;
    private ShopAd shopAd;

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

    @Override
    public StepperModel createNewStepperModel() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            shopAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
        }
        return null;
    }

    public static Intent createIntent(Context context, ShopAd shopAd){
        Intent intent = new Intent(context, TopAdsCreatePromoShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, shopAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, shopAd.getName());
        return intent;
    }


    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public ShopAd getShopAd() {
        return shopAd;
    }
}
