package com.tokopedia.seller.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsDetailEditShopFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsDetailNewGroupFragment;

/**
 * Activity to Create New TopAds Group
 * Pass GroupId if the group has already existed
 * Pass group name to show the group to be created/edited
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDetailNewGroupActivity extends TActivity {

    public static void startNewGroup(Activity activity, int requestCode,
                                     String groupName, String itemIdToAdd){
        Intent intent = new Intent(activity, TopAdsDetailNewGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startEditExistingGroup(Activity activity, int requestCode,
                                              String groupId, String groupName, String itemIdToAdd){
        Intent intent = new Intent(activity, TopAdsDetailNewGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        intent.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_edit_promo);
        String name = null;
        String adId = null;
        String itemIdToAdd = null;
        Intent intent = getIntent();
        if (intent!= null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            name = extras.getString(TopAdsExtraConstant.EXTRA_NAME);
            adId = extras.getString(TopAdsExtraConstant.EXTRA_AD_ID);
            itemIdToAdd = extras.getString(TopAdsExtraConstant.EXTRA_ITEM_ID);
        }
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailNewGroupFragment.createInstance(name, adId, itemIdToAdd), TopAdsDetailEditShopFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}