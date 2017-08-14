package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostExistingGroupFragment extends TopAdsNewCostFragment {

    @Inject
    TopAdsDetailEditGroupPresenter topAdsDetailEditGroupPresenter;

    @Override
    protected void onClickedNext() {
//        topAdsDetailEditGroupPresenter.saveAd();
    }

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditCostExistingGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
