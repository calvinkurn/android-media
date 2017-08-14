package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostShopFragment extends TopAdsNewCostFragment {
    @Override
    protected void onClickedNext() {

    }

    public static Fragment createInstance(String shopId) {
        Fragment fragment = new TopAdsEditCostShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
