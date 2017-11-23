package com.tokopedia.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordEditDetailNegativeFragment;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/30/17.
 */

public class TopAdsKeywordEditDetailNegativeActivity extends TopAdsKeywordEditDetailPositiveActivity {

    public static Intent createInstance(Context context, KeywordAd keywordAd){
        Intent starter = new Intent(context, TopAdsKeywordEditDetailNegativeActivity.class);
        starter.putExtra(TopAdsExtraConstant.EXTRA_AD, keywordAd);
        return starter;
    }

    @Override
    protected Fragment getNewFragment() {
        KeywordAd keywordAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
        return TopAdsKeywordEditDetailNegativeFragment.createInstance(keywordAd);
    }
}