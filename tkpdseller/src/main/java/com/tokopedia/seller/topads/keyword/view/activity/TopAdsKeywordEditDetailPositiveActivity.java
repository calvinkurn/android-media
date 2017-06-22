package com.tokopedia.seller.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailPositiveFragment;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/23/17.
 */

public class TopAdsKeywordEditDetailPositiveActivity extends BaseSimpleActivity {

    public static Intent createInstance(Context context, KeywordAd keywordAd){
        Intent intent = new Intent(context, TopAdsKeywordEditDetailPositiveActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, keywordAd);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        KeywordAd keywordAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
        return TopAdsKeywordEditDetailPositiveFragment.createInstance(keywordAd);
    }

    @Override
    protected String getTagFragment() {
        return TopAdsKeywordEditDetailPositiveActivity.class.getSimpleName();
    }
}