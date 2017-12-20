package com.tokopedia.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoExistingGroupModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoWithoutGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoWithoutGroupActivity extends BaseStepperActivity<TopAdsCreatePromoWithoutGroupModel> implements HasComponent<AppComponent> {
    private List<Fragment> fragmentList;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public TopAdsCreatePromoWithoutGroupModel createNewStepperModel() {
        String itemIdToAdd = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            itemIdToAdd = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
        }
        stepperModel = new TopAdsCreatePromoWithoutGroupModel();
        ((TopAdsCreatePromoWithoutGroupModel)stepperModel).setIdToAdd(itemIdToAdd);
        return stepperModel;
    }

    public static Intent createIntent(Context context, String itemIdToAdd){
        Intent intent = new Intent(context, TopAdsCreatePromoWithoutGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
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


    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
