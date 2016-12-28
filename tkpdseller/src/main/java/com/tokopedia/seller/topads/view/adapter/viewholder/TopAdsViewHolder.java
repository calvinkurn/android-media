package com.tokopedia.seller.topads.view.adapter.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.Ad;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 11/28/16.
 */
public class TopAdsViewHolder extends SwappingHolder {

    @BindView(R2.id.title_product)
    public TextView titleProduct;

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

    @BindView(R2.id.progressBarPromo)
    public ProgressBar progressBarPromo;

    @BindView(R2.id.mainView)
    public View mainView;

    public TopAdsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindObject(Ad ad) {
        titleProduct.setText(ad.getName());
        statusActive.setText(ad.getStatusDesc());
        promoPriceUsed.setText(ad.getPriceDailyFmt());
        totalPricePromo.setText(ad.getPriceDailySpentFmt());
        progressBarPromo.setProgress(2);
    }

    public TopAdsViewHolder(Context context, ViewGroup parent, MultiSelector multiSelector){
        super(LayoutInflater.from(context).inflate(R.layout.list_promo_single_topads, parent, false), multiSelector);
    }
}
