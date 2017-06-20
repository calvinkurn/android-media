package com.tokopedia.seller.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordDetailFragment;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailActivity extends TopAdsBaseSimpleActivity implements HasComponent<AppComponent> {

    public static Intent createInstance(Context context, KeywordAd keywordAd, String adId){
        Intent intent = new Intent(context, TopAdsKeywordDetailActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, keywordAd);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        KeywordAd keywordAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_AD);
        String adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
        return TopAdsKeywordDetailFragment.createInstance(keywordAd, adId);
    }

    @Override
    protected String getTagFragment() {
        return TopAdsKeywordDetailActivity.class.getSimpleName();
    }
}
