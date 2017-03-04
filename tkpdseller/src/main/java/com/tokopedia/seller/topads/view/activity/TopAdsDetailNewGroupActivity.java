package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailEditShopFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewGroupFragment;

/**
 * Activity to Create New TopAds Group
 * Pass GroupId if the group has already existed
 * Pass group name to show the group to be created/edited
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDetailNewGroupActivity extends TActivity {

    // TODO set result OK if edit/new group success

    public static void startNewGroup(Activity activity, int requestCode,
                                     String groupName){
        Intent intent = new Intent(activity, TopAdsDetailNewGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startEditExistingGroup(Activity activity, int requestCode,
                                              int groupId, String groupName){
        Intent intent = new Intent(activity, TopAdsDetailNewGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_ID, groupId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP_NAME, groupName);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_edit_promo);
        String groupName = null;
        int groupId = 0;
        if (getIntent() != null && getIntent().getExtras() != null) {
            groupName = getIntent().getExtras().getString(TopAdsExtraConstant.EXTRA_GROUP_NAME);
            groupId = getIntent().getExtras().getInt(TopAdsExtraConstant.EXTRA_GROUP_ID);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailNewGroupFragment.createInstance(groupName, groupId), TopAdsDetailEditShopFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}