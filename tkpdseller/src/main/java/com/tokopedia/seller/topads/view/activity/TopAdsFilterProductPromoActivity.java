package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterEtalaseFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterGroupNameFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusPromoFragment;

import java.util.ArrayList;
import java.util.List;

public class TopAdsFilterProductPromoActivity extends TopAdsFilterActivity {

    private int selectedStatusPromo;
    private int selectedEtalaseId;

    public static void start(Activity activity, int requestCode,
                             int selectedStatusPromo, int etalaseId){
        Intent intent = new Intent(activity, TopAdsFilterProductPromoActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, selectedStatusPromo);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, etalaseId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, int requestCode){
        start(activity, requestCode, 0, 0);
    }

    @Override
    protected void setupVar() {
        super.setupVar();
        // TODO Hendry remove, this is just for testing
        selectedStatusPromo = 0;
        selectedEtalaseId = 0;
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