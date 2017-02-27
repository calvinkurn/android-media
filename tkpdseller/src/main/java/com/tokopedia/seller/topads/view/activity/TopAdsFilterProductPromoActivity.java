package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterEtalaseFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusPromoFragment;

import java.util.ArrayList;
import java.util.List;

/*
 * Digunakan untuk memfilter Product y
 */
public class TopAdsFilterProductPromoActivity extends TopAdsFilterActivity {

    private int selectedStatusPromo;
    private int selectedEtalaseId;

    public static void start(Activity activity, int requestCode,
                             int selectedStatusPromo, int etalaseId){
        Intent intent = createIntent(activity, selectedStatusPromo, etalaseId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             int selectedStatusPromo, int etalaseId){
        Intent intent = createIntent(context, selectedStatusPromo, etalaseId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(android.support.v4.app.Fragment fragment, Context context, int requestCode,
                             int selectedStatusPromo, int etalaseId){
        Intent intent = createIntent(context, selectedStatusPromo, etalaseId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static Intent createIntent(Context context, int selectedStatusPromo, int selectedEtalaseId){
        Intent intent = new Intent(context, TopAdsFilterProductPromoActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, selectedStatusPromo);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, selectedEtalaseId);
        return intent;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedStatusPromo = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO);
        selectedEtalaseId = extras.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE);
    }

    @Override
    protected List<TopAdsFilterContentFragment> getFilterContentList() {
        List<TopAdsFilterContentFragment> filterContentFragmentList = new ArrayList<>();
        TopAdsFilterEtalaseFragment topAdsFilterStatusFragment = TopAdsFilterEtalaseFragment.createInstance(selectedEtalaseId);
        topAdsFilterStatusFragment.setActive(true);
        filterContentFragmentList.add(topAdsFilterStatusFragment);

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