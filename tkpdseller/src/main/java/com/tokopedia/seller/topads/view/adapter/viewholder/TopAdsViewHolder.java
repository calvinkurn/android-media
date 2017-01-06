package com.tokopedia.seller.topads.view.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.model.data.Ad;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 11/28/16.
 */
public class TopAdsViewHolder extends SwappingHolder {

    @BindView(R2.id.title_product)
    public TextView titleProduct;

    @BindView(R2.id.status_active_dot)
    public View statusActiveDot;

    @BindView(R2.id.status_active)
    public TextView statusActive;

    @BindView(R2.id.promo_price_used)
    public TextView promoPriceUsed;

    @BindView(R2.id.total_price_promo)
    public TextView totalPricePromo;

    @BindView(R2.id.price_promo_per_click)
    public TextView pricePromoPerClick;

    @BindView(R2.id.check_promo)
    public CheckBox checkedPromo;

    @BindView(R2.id.progress_bar_layout)
    public View progressBarLayout;

    @BindView(R2.id.progress_bar)
    public ProgressBar progressBarPromo;

    @BindView(R2.id.mainView)
    public View mainView;

    public TopAdsViewHolder(View view, MultiSelector multiSelector) {
        super(view, multiSelector);
        ButterKnife.bind(this, view);
    }

    public void bindObject(Ad ad) {
        titleProduct.setText(ad.getName());
        statusActive.setText(ad.getStatusDesc());
        if (ad.isStatusActive()) {
            statusActiveDot.setBackgroundResource(R.drawable.green_circle);
        } else {
            statusActiveDot.setBackgroundResource(R.drawable.grey_circle);
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