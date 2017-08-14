package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditScheduleExistingGroupFragment extends TopAdsNewScheduleFragment {
    @Inject
    TopAdsDetailEditGroupPresenter topAdsDetailEditGroupPresenter;

    @Override
    protected void onNextClicked() {
        topAdsDetailEditGroupPresenter.saveAd((TopAdsDetailGroupViewModel)detailAd);
    }

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditCostExistingGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
