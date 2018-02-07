package com.tokopedia.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseFilterActivity;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsFilterEtalaseFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsFilterStatusPromoFragment;

import java.util.ArrayList;
import java.util.List;

/*
 * Digunakan untuk memfilter Product y
 */
public class TopAdsFilterProductPromoActivity extends BaseFilterActivity {

    private int selectedStatusPromo;
    private int selectedEtalaseId;
    private boolean isHideEtalase;

    public static void start(Activity activity, int requestCode,
                             int selectedStatusPromo, int etalaseId, boolean isHideEtalase){
        Intent intent = createIntent(activity, selectedStatusPromo, etalaseId, isHideEtalase);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             int selectedStatusPromo, int etalaseId, boolean isHideEtalase){
        Intent intent = createIntent(context, selectedStatusPromo, etalaseId, isHideEtalase);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(android.app.Fragment fragment, Context context, int requestCode,
                             int selectedStatusPromo, int etalaseId, boolean isHideEtalase){
        Intent intent = createIntent(context, selectedStatusPromo, etalaseId, isHideEtalase);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static Intent createIntent(Context context,
                                      int selectedStatusPromo,
                                      int selectedEtalaseId,
                                      boolean isHideEtalase){
        Intent intent = new Intent(context, TopAdsFilterProductPromoActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, selectedStatusPromo);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, selectedEtalaseId);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_HIDE_ETALASE, isHideEtalase);
        return intent;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedStatusPromo = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO);
        selectedEtalaseId = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE);
        isHideEtalase = extras.getBoolean(TopAdsExtraConstant.EXTRA_FILTER_HIDE_ETALASE);
    }

    @Override
    protected List<Fragment> getFilterContentList() {
        List<Fragment> filterContentFragmentList = new ArrayList<>();

        if (!isHideEtalase) {
            TopAdsFilterEtalaseFragment topAdsFilterStatusFragment = TopAdsFilterEtalaseFragment.createInstance(selectedEtalaseId);
            topAdsFilterStatusFragment.setActive(true);
            filterContentFragmentList.add(topAdsFilterStatusFragment);
        }

        TopAdsFilterStatusPromoFragment topAdsFilterStatusPromoFragment = TopAdsFilterStatusPromoFragment.createInstance(selectedStatusPromo);
        topAdsFilterStatusPromoFragment.setActive(true);
        filterContentFragmentList.add(topAdsFilterStatusPromoFragment);

        return filterContentFragmentList;
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, selectedStatusPromo);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, selectedEtalaseId);
        return intent;
    }

    @Override
    public String getScreenName() {
        return null;
    }

}