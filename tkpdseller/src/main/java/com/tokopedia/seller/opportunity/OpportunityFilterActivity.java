package com.tokopedia.seller.opportunity;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.opportunity.fragment.OpportunityCategoryFragment;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.activity.TopAdsFilterActivity;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.seller.topads.constant.TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityFilterActivity extends TopAdsFilterActivity {

    public static final String EXTRA_CATEGORY_SELECTED_VALUE = "EXTRA_CATEGORY_SELECTED_VALUE";
    private int selectedCategoryValue;

    @Override
    protected void setupBundlePass(Bundle extras) {
        super.setupBundlePass(extras);
        selectedCategoryValue = extras.getInt(EXTRA_CATEGORY_SELECTED_VALUE);
    }

    @Override
    protected List<TopAdsFilterContentFragment> getFilterContentList() {
        List<TopAdsFilterContentFragment> filterContentFragmentList = new ArrayList<>();

        OpportunityCategoryFragment opportunityCategoryFragment =
                OpportunityCategoryFragment.createInstance(selectedCategoryValue);
        opportunityCategoryFragment.setActive(false);

        TopAdsFilterStatusFragment topAdsFilterStatusFragment =
                TopAdsFilterStatusFragment.createInstance(0);
        topAdsFilterStatusFragment.setActive(false);

        filterContentFragmentList.add(topAdsFilterStatusFragment);
        filterContentFragmentList.add(opportunityCategoryFragment);
        return filterContentFragmentList;
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CATEGORY_SELECTED_VALUE, selectedCategoryValue);
        return intent;
    }

    @Override
    public String getScreenName() {
        return null;
    }
}