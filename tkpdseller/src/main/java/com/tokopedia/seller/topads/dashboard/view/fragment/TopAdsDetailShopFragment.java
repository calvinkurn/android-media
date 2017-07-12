package com.tokopedia.seller.topads.dashboard.view.fragment;

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
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.dashboard.data.source.local.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsShopAdInteractorImpl;
import com.tokopedia.seller.topads.dashboard.view.model.Ad;
import com.tokopedia.seller.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailEditShopActivity;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailShopPresenterImpl;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailShopFragment extends TopAdsDetailStatisticFragment<TopAdsDetailProductPresenter> {

    public static final String SHOP_AD_PARCELABLE = "SHOP_AD_PARCELABLE";
    private MenuItem deleteMenuItem;
    private ShopAd shopAd;

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
        presenter = new TopAdsDetailShopPresenterImpl(getActivity(), this,
                new TopAdsProductAdInteractorImpl(new TopAdsManagementService(new SessionHandler(getActivity()).getAccessToken(getActivity())), new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(getActivity())),
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
        presenter.turnOnAds(shopAd.getId());
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        presenter.turnOffAds(shopAd.getId());
    }

    @Override
    protected void refreshAd() {
        if (shopAd != null) {
            presenter.refreshAd(startDate, endDate, shopAd.getId());
        } else {
            presenter.refreshAd(startDate, endDate, adId);
        }
    }

    @Override
    protected void editAd() {
        if (shopAd != null) {
            Intent intent = new Intent(getActivity(), TopAdsDetailEditShopActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, shopAd.getName());
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, String.valueOf(shopAd.getId()));
            startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        shopAd = (ShopAd) ad;
        super.onAdLoaded(ad);
    }

    void onNameClicked() {
        DeepLinkChecker.openShop(shopAd.getShopUri(), getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        deleteMenuItem = menu.findItem(R.id.menu_delete);
        deleteMenuItem.setVisible(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SHOP_AD_PARCELABLE, shopAd);
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        shopAd = savedInstanceState.getParcelable(SHOP_AD_PARCELABLE);
        onAdLoaded(shopAd);
    }
}