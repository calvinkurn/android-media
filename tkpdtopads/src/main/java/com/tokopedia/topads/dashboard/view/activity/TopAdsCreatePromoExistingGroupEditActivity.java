package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupEditFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/29/17.
 */

public class TopAdsCreatePromoExistingGroupEditActivity extends TopAdsCreatePromoExistingGroupActivity {

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopAdsNewProductListExistingGroupEditFragment());
            return fragmentList;
        }else{
            return fragmentList;
        }
    }

    public static Intent createIntent(Context context, String groupId, String itemIdToAdd) {
        Intent intent = new Intent(context, TopAdsCreatePromoExistingGroupEditActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        return intent;
    }
}
