package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.ShopAd;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardShopPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailShopActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsStatisticShopActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardStoreFragmentListener;

public class TopAdsDashboardShopFragment extends TopAdsDashboardFragment<TopAdsDashboardShopPresenterImpl> implements TopAdsDashboardStoreFragmentListener {

    protected static final int REQUEST_CODE_AD_STATUS = TopAdsDashboardShopFragment.class.hashCode();

    View shopAdView;
    View shopAdEmptyView;
    TextView titleProduct;
    View statusActiveDot;
    TextView statusActive;
    TextView promoPriceUsed;
    TextView pricePromoPerClick;

    @BindView(R2.id.image_button_add_deposit)
    ImageView imageButtonAddDeposit;

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
        titleProduct = (TextView) view.findViewById(R.id.title_product);
        statusActiveDot = view.findViewById(R.id.status_active_dot);
        statusActive = (TextView) view.findViewById(R.id.status_active);
        promoPriceUsed = (TextView) view.findViewById(R.id.promo_price_used);
        pricePromoPerClick = (TextView) view.findViewById(R.id.price_promo_per_click);
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
        if (ad.getId() > 0) {
            loadDetailAd(ad);
        } else {
            loadAdShopNotExist();
        }
        onLoadDataSuccess();
    }

    private void loadDetailAd(ShopAd ad) {
        shopAdEmptyView.setVisibility(View.GONE);
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
        promoPriceUsed.setText(ad.getStatTotalSpent());
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