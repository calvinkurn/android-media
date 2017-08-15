package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoExistingGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsCreatePromoExistingGroupActivity extends BaseStepperActivity {
    List<Fragment> fragmentList;

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewProductListExistingGroupFragment());
            return fragmentList;
        }else{
            return fragmentList;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String groupId = null;
        String itemIdToAdd = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            groupId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
            itemIdToAdd = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
        }
        stepperModel = new TopAdsCreatePromoExistingGroupModel();
        ((TopAdsCreatePromoExistingGroupModel)stepperModel).setGroupId(groupId);
        ((TopAdsCreatePromoExistingGroupModel)stepperModel).setIdToAdd(itemIdToAdd);
        super.onCreate(savedInstanceState);
    }

    public static Intent createIntent(Context context, String groupId, String itemIdToAdd) {
        Intent intent = new Intent(context, TopAdsCreatePromoExistingGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        return intent;
    }
}
