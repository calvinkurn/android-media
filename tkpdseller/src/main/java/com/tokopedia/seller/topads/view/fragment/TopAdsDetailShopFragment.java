package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.data.source.local.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.domain.interactor.TopAdsShopAdInteractorImpl;
import com.tokopedia.seller.topads.domain.model.data.Ad;
import com.tokopedia.seller.topads.domain.model.data.ShopAd;
import com.tokopedia.seller.topads.view.activity.TopAdsEditPromoShopActivity;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailShopPresenterImpl;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailShopFragment extends TopAdsDetailFragment<TopAdsDetailProductPresenter> {

    private MenuItem deleteMenuItem;
    private ShopAd ad;

    public static Fragment createInstance(ShopAd shopAd) {
        Fragment fragment = new TopAdsDetailShopFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, shopAd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailShopPresenterImpl(getActivity(), this,
                new TopAdsProductAdInteractorImpl(new TopAdsManagementService(), new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(getActivity())),
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
        presenter.refreshAd(startDate, endDate, ad.getId());
    }

    @Override
    protected void editAd() {
        Intent intent = new Intent(getActivity(), TopAdsEditPromoShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, ad);
        startActivity(intent);
    }

    @Override
    protected void deleteAd() {
        super.deleteAd();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        super.onAdLoaded(ad);
        this.ad = (ShopAd) ad;
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