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
import com.tokopedia.seller.topads.model.data.Ad;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zulfikarrahman on 11/28/16.
 */
public class TopAdsSingleViewHolder extends RecyclerView.ViewHolder {

    @Bind(R2.id.title_product)
    TextView titleProduct;

    @Bind(R2.id.status_active)
    TextView statusActive;

    @Bind(R2.id.promo_price_used)
    TextView promoPriceUsed;

    @Bind(R2.id.total_price_promo)
    TextView totalPricePromo;

    @Bind(R2.id.price_promo_per_klik)
    TextView pricePromoPerKlik;

    @Bind(R2.id.check_promo)
    CheckBox checkedPromo;

    @Bind(R2.id.progressBarPromo)
    ProgressBar progressBarPromo;

    public TopAdsSingleViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public static TopAdsSingleViewHolder createInstance(Context context, ViewGroup parent){
        return new TopAdsSingleViewHolder(LayoutInflater.from(context).inflate(R.layout.list_promo_single_topads, parent, false));
    }

    public void bindData(Ad ad) {
        titleProduct.setText(ad.getProductName());
        statusActive.setText(ad.getAdStatus());
        promoPriceUsed.setText(ad.getAdPriceBidFmt());
        totalPricePromo.setText(ad.getAdPriceDailySpentFmt());

    }
}
