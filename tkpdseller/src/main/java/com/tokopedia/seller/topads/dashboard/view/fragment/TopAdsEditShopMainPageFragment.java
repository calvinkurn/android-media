package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.dashboard.data.source.local.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsShopAdInteractorImpl;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsEditCostShopActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsEditScheduleExistingGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsEditScheduleShopActivity;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailShopPresenterImpl;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailShopViewPresenterImpl;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditShopMainPageFragment extends TopAdsDetailEditMainPageFragment<ShopAd> {

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditShopMainPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailShopPresenterImpl(getActivity(), this,
                new TopAdsShopAdInteractorImpl(getActivity()));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_shop_main_page;
    }

    @Override
    protected void refreshAd() {
        if (ad != null) {
            presenter.refreshAd(startDate, endDate, ad.getId());
        } else {
            presenter.refreshAd(startDate, endDate, adId);
        }
    }

    @Override
    protected void onScheduleClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsEditScheduleShopActivity.createIntent(getActivity(), String.valueOf(ad.getId()));
        }else{
            intent = TopAdsEditScheduleShopActivity.createIntent(getActivity(), adId);
        }
        startActivity(intent);
    }

    @Override
    protected void onCostClicked() {
        Intent intent;
        if(ad!= null) {
            intent = TopAdsEditCostShopActivity.createIntent(getActivity(), String.valueOf(ad.getId()));
        }else{
            intent = TopAdsEditCostShopActivity.createIntent(getActivity(), adId);
        }
        startActivity(intent);
    }
}
