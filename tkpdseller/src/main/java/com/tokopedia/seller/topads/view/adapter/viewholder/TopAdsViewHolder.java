package com.tokopedia.seller.topads.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.model.data.Ad;

/**
 * Created by zulfikarrahman on 11/28/16.
 */
public class TopAdsViewHolder extends RecyclerView.ViewHolder {

    public TextView titleProduct;
    public View statusActiveDot;
    public TextView statusActive;
    public TextView promoPriceUsed;
    public TextView totalPricePromo;
    public TextView pricePromoPerClick;
    public View progressBarLayout;
    public ProgressBar progressBarPromo;
    public View mainView;

    public TopAdsViewHolder(View view) {
        super(view);
        titleProduct = (TextView) view.findViewById(R.id.title_product);
        statusActiveDot = view.findViewById(R.id.status_active_dot);
        statusActive = (TextView) view.findViewById(R.id.status_active);
        promoPriceUsed = (TextView) view.findViewById(R.id.promo_price_used);
        totalPricePromo = (TextView) view.findViewById(R.id.total_price_promo);
        pricePromoPerClick = (TextView) view.findViewById(R.id.price_promo_per_click);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        progressBarPromo = (ProgressBar) view.findViewById(R.id.progress_bar);
        mainView = view.findViewById(R.id.mainView);
    }

    public void bindObject(Ad ad) {
        titleProduct.setText(ad.getName());
        statusActive.setText(ad.getStatusDesc());
        switch (ad.getStatus()) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
                statusActiveDot.setBackgroundResource(R.drawable.green_circle);
                break;
            default:
                statusActiveDot.setBackgroundResource(R.drawable.grey_circle);
                break;
        }
        pricePromoPerClick.setText(promoPriceUsed.getContext().getString(R.string.top_ads_bid_format_text, ad.getPriceBidFmt(), ad.getLabelPerClick()));
        promoPriceUsed.setText(promoPriceUsed.getContext().getString(R.string.top_ads_used_format_text, ad.getStatTotalSpent()));
        if (!TextUtils.isEmpty(ad.getPriceDailyBar())) {
            progressBarLayout.setVisibility(View.VISIBLE);
            progressBarPromo.setProgress((int) Double.parseDouble(ad.getPriceDailyBar()));
            totalPricePromo.setText(promoPriceUsed.getContext().getString(R.string.top_ads_bid_format_text, ad.getPriceDailySpentFmt(), ad.getPriceDailyFmt()));
        } else {
            progressBarLayout.setVisibility(View.GONE);
        }
    }
}