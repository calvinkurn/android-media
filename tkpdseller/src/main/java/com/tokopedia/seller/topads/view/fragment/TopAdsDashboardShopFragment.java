package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.ShopAd;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailNewShopActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailShopActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsStatisticShopActivity;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardStoreFragmentListener;
import com.tokopedia.seller.topads.view.presenter.TopAdsDashboardShopPresenterImpl;

public class TopAdsDashboardShopFragment extends TopAdsDashboardFragment<TopAdsDashboardShopPresenterImpl> implements TopAdsDashboardStoreFragmentListener {

    protected static final int REQUEST_CODE_AD_STATUS = 2;

    View shopAdView;
    View shopAdEmptyView;

    private ShopAd shopAd;

    public static TopAdsDashboardShopFragment createInstance() {
        TopAdsDashboardShopFragment fragment = new TopAdsDashboardShopFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDashboardShopPresenterImpl(getActivity());
        presenter.setTopAdsDashboardFragmentListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_store;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        shopAdView = view.findViewById(R.id.layout_shop_ad);
        shopAdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShopItemClicked();
            }
        });
        shopAdEmptyView = view.findViewById(R.id.layout_empty_shop_ad);
        Button addPromoButton = (Button) view.findViewById(R.id.button_add_promo);
        addPromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateShop();
            }
        });
    }

    public void onCreateShop() {
        Intent intent = new Intent(getActivity(), TopAdsDetailNewShopActivity.class);
        if (!TextUtils.isEmpty(shopAd.getName())) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, shopAd.getName());
        }
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    public void loadData() {
        super.loadData();
        populateShop();
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        super.onRestoreState(savedState);
    }

    private void populateShop() {
        if (startDate == null && endDate == null) {
            startDate = datePickerPresenter.getStartDate();
            endDate = datePickerPresenter.getEndDate();
        }
        presenter.populateShopAd(startDate, endDate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_STATUS && intent != null) {
            boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            if (adStatusChanged) {
                populateShop();
            }
        }
    }

    @Override
    public void onAdShopLoaded(@NonNull ShopAd ad) {
        shopAd = ad;
        if (ad.getId() > 0) {
            loadDetailAd(ad);
        } else {
            loadAdShopNotExist();
        }
        onLoadDataSuccess();
    }

    private void loadDetailAd(ShopAd ad) {
        shopAdView.setVisibility(View.VISIBLE);
        shopAdEmptyView.setVisibility(View.GONE);
        TopAdsViewHolder topAdsViewHolder = new TopAdsViewHolder(shopAdView);
        topAdsViewHolder.bindObject(ad);
    }

    private void loadAdShopNotExist() {
        shopAdView.setVisibility(View.GONE);
        shopAdEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadAdShopError() {
        showNetworkError();
        hideLoading();
    }

    @Override
    protected Class<?> getClassIntentStatistic() {
        return TopAdsStatisticShopActivity.class;
    }

    void onShopItemClicked() {
        Intent intent = new Intent(getActivity(), TopAdsDetailShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, shopAd);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }
}