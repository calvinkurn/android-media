package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ShopAd;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardShopPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsStatisticShopActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailShopActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardStoreFragmentListener;

import butterknife.BindView;
import butterknife.OnClick;

public class TopAdsDashboardShopFragment extends TopAdsDashboardFragment<TopAdsDashboardShopPresenterImpl> implements TopAdsDashboardStoreFragmentListener {

    protected static final int REQUEST_CODE_AD_STATUS = TopAdsDashboardShopFragment.class.hashCode();

    @BindView(R2.id.layout_shop_ad)
    View shopAdView;

    @BindView(R2.id.title_product)
    TextView titleProduct;

    @BindView(R2.id.status_active_dot)
    View statusActiveDot;

    @BindView(R2.id.status_active)
    TextView statusActive;

    @BindView(R2.id.promo_price_used)
    TextView promoPriceUsed;

    @BindView(R2.id.price_promo_per_click)
    TextView pricePromoPerClick;

    private ShopAd shopAd;

    public static TopAdsDashboardShopFragment createInstance() {
        TopAdsDashboardShopFragment fragment = new TopAdsDashboardShopFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
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
    }

    public void loadData() {
        super.loadData();
        populateShop();
    }

    private void populateShop() {
        presenter.populateShopAd(startDate, endDate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_STATUS && intent != null) {
            boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_STATUS_CHANGED, false);
            if (adStatusChanged) {
                populateShop();
            }
        }
    }

    @Override
    public void onAdShopLoaded(@NonNull ShopAd ad) {
        shopAd = ad;
        shopAdView.setVisibility(View.VISIBLE);
        titleProduct.setText(ad.getName());
        statusActive.setText(ad.getStatusDesc());
        switch (ad.getStatus()) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
                statusActiveDot.setBackgroundResource(R.drawable.green_circle);
                break;
            case TopAdsConstant.STATUS_AD_NOT_ACTIVE:
                statusActiveDot.setBackgroundResource(R.drawable.grey_circle);
                break;
            case TopAdsConstant.STATUS_AD_NOT_SENT:
                statusActiveDot.setBackgroundResource(R.drawable.grey_circle);
                break;
        }
        pricePromoPerClick.setText(promoPriceUsed.getContext().getString(R.string.top_ads_bid_format_text, ad.getPriceBidFmt(), ad.getLabelPerClick()));
        promoPriceUsed.setText(promoPriceUsed.getContext().getString(R.string.top_ads_used_format_text, ad.getStatTotalSpent()));
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

    @OnClick(R2.id.layout_shop_ad)
    void onShopItemClicked() {
        Intent intent = new Intent(getActivity(), TopAdsDetailShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD, shopAd);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }
}