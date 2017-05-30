package com.tokopedia.seller.topads.keyword.view.adapter.viewholder;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.domain.model.Datum;

/**
 * Created by normansyahputa on 5/19/17.
 */

public class TopAdsKeywordViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleProduct;
    private final TextView statusActive;
    private final TextView pricePromoPerClick;
    private final TextView promoPriceUsed;
    private final TextView groupName;

    public TopAdsKeywordViewHolder(View itemView) {
        super(itemView);
        titleProduct = (TextView) itemView.findViewById(R.id.title_product);
        statusActive = (TextView) itemView.findViewById(R.id.status_active);
        pricePromoPerClick = (TextView) itemView.findViewById(R.id.price_promo_per_click);
        promoPriceUsed = (TextView) itemView.findViewById(R.id.promo_price_used);
        groupName = (TextView) itemView.findViewById(R.id.group_name);

    }

    private String getString(@StringRes int stringRes) {
        return itemView.getContext().getString(stringRes);
    }

    private String getString(@StringRes int stringRes, String value) {
        return itemView.getContext().getString(stringRes, value);
    }


    public void bindDataAds(final Datum datum) {
        titleProduct.setText(datum.getKeywordTag());
        statusActive.setText(datum.getKeywordStatusDesc());
        pricePromoPerClick.setText(getString(R.string.top_ads_per_click_detail, datum.getKeywordPriceBidFmt()));
        promoPriceUsed.setText(datum.getStatTotalSpent());
        groupName.setText(datum.getGroupName());
    }
}
