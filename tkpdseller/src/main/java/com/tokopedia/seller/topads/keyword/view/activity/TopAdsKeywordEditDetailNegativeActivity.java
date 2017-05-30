package com.tokopedia.seller.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailNegativeFragment;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/30/17.
 */

public class TopAdsKeywordEditDetailNegativeActivity extends TopAdsKeywordEditDetailPositiveActivity {
    public static void start(Context context, KeywordAd model) {
        context.startActivity(createIntent(context, model));
    }

    public static Intent createIntent(Context context, KeywordAd model){
        Intent starter = new Intent(context, TopAdsKeywordEditDetailNegativeActivity.class);
        starter.putExtra(KEYWORD_DETAIL_MODEL, model);
        return starter;
    }

    @Override
    protected TopAdsKeywordEditDetailFragment getFragment(KeywordAd model) {
        return new TopAdsKeywordEditDetailNegativeFragment();
    }
}
