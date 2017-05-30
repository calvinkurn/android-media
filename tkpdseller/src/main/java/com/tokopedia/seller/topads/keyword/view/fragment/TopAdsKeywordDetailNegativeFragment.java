package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public class TopAdsKeywordDetailNegativeFragment extends TopAdsKeywordDetailFragment {

    public static Fragment createInstance(KeywordAd ad, String adId) {
        Fragment fragment = new TopAdsKeywordDetailNegativeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }


}
