package com.tokopedia.seller.topads.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.seller.R2;
import com.tokopedia.seller.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 11/28/16.
 */
public class TopAdsSingleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.title_product)
    public TextView titleProduct;

    @BindView(R2.id.status_active)
    public TextView statusActive;

    @BindView(R2.id.promo_price_used)
    public TextView promoPriceUsed;

    @BindView(R2.id.total_price_promo)
    public TextView totalPricePromo;

    @BindView(R2.id.price_promo_per_klik)
    public TextView pricePromoPerKlik;

    @BindView(R2.id.check_promo)
    public CheckBox checkedPromo;

    @BindView(R2.id.progressBarPromo)
    public ProgressBar progressBarPromo;

    public TopAdsSingleViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public static TopAdsSingleViewHolder createInstance(Context context, ViewGroup parent){
        return new TopAdsSingleViewHolder(LayoutInflater.from(context).inflate(R.layout.list_promo_single_topads, parent, false));
    }


}
