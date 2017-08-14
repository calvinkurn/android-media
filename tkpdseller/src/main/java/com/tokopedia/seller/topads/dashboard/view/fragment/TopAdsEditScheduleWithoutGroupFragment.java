package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditScheduleWithoutGroupFragment extends TopAdsNewScheduleFragment {

    @Inject
    TopAdsDetailEditProductPresenter topAdsDetailEditProductPresenter;

    @Override
    protected void onNextClicked() {
//        topAdsDetailEditProductPresenter.saveAd();
    }

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditScheduleWithoutGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
