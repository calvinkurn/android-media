package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsShopAdInteractorImpl;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditShopMainPageActivity;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailShopViewPresenterImpl;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailShopFragment extends TopAdsDetailStatisticFragment<TopAdsDetailProductPresenter, ShopAd> {

    public static final String SHOP_AD_PARCELABLE = "SHOP_AD_PARCELABLE";
    private MenuItem deleteMenuItem;

    public static Fragment createInstance(ShopAd shopAd, String adId) {
        Fragment fragment = new TopAdsDetailShopFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, shopAd);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailShopViewPresenterImpl(getActivity(), this,
                new TopAdsProductAdInteractorImpl(new TopAdsManagementService(new SessionHandler(getActivity())),
                        new TopAdsCacheDataSourceImpl(getActivity())),
                new TopAdsShopAdInteractorImpl(getActivity()));
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        name.setTitle(getString(R.string.title_top_ads_store));
        name.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
        favorite.setTitle(getString(R.string.label_top_ads_favorit));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNameClicked();
            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_shop_detail;
    }

    @Override
    protected void turnOnAd() {
        super.turnOnAd();
        presenter.turnOnAds(ad.getId());
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        presenter.turnOffAds(ad.getId());
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
    protected void editAd() {
        if (ad != null) {
            Intent intent = TopAdsEditShopMainPageActivity.createIntent(getActivity(), ad.getId());
            startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
        }
    }

    void onNameClicked() {
        DeepLinkChecker.openShop(ad.getShopUri(), getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        deleteMenuItem = menu.findItem(R.id.menu_delete);
        deleteMenuItem.setVisible(false);
    }
}