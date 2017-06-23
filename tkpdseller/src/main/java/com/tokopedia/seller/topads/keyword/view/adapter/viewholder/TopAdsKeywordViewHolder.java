package com.tokopedia.seller.topads.keyword.view.adapter.viewholder;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.model.NegativeKeywordAd;

/**
 * Created by normansyahputa on 5/19/17.
 */

public class TopAdsKeywordViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleProduct;
    private final TextView statusActive;
    private final TextView pricePromoPerClick;
    private final TextView promoPriceUsed;
    private final TextView groupName;
    private final View statusActiveDot;
    private final LinearLayout promoPriceUsedContainer;
    private final TextView keywordTypeDescription;
    private final LinearLayout statusActiveContainer;

    public TopAdsKeywordViewHolder(View itemView) {
        super(itemView);
        titleProduct = (TextView) itemView.findViewById(R.id.title_product);
        statusActive = (TextView) itemView.findViewById(R.id.status_active);
        pricePromoPerClick = (TextView) itemView.findViewById(R.id.price_promo_per_click);
        promoPriceUsed = (TextView) itemView.findViewById(R.id.promo_price_used);
        groupName = (TextView) itemView.findViewById(R.id.group_name);
        statusActiveDot = itemView.findViewById(R.id.status_active_dot);
        promoPriceUsedContainer = (LinearLayout) itemView.findViewById(R.id.promo_price_used_container);
        keywordTypeDescription = (TextView) itemView.findViewById(R.id.title_keyword_type_description);
        statusActiveContainer = (LinearLayout) itemView.findViewById(R.id.status_active_container);
    }

    private String getString(@StringRes int stringRes) {
        return itemView.getContext().getString(stringRes);
    }

    private String getString(@StringRes int stringRes, String value) {
        return itemView.getContext().getString(stringRes, value);
    }

    public void bindObject(final KeywordAd keywordAd) {
        if (keywordAd != null && keywordAd instanceof NegativeKeywordAd) {
            promoPriceUsedContainer.setVisibility(View.GONE);
            pricePromoPerClick.setVisibility(View.GONE);
            statusActiveContainer.setVisibility(View.GONE);
        }
        keywordTypeDescription.setText(keywordAd.getKeywordTypeDesc());
        titleProduct.setText(keywordAd.getKeywordTag());
        statusActive.setText(keywordAd.getStatusDesc());
        pricePromoPerClick.setText(getString(R.string.top_ads_per_click_detail, keywordAd.getPriceBidFmt()));
        promoPriceUsed.setText(keywordAd.getStatTotalSpent());
        groupName.setText(itemView.getContext().getString(R.string.top_ads_keywords_groups_format, keywordAd.getGroupName()));
        switch (keywordAd.getStatus()) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
                statusActiveDot.setBackgroundResource(R.drawable.ic_status_green);
                break;
            default:
                statusActiveDot.setBackgroundResource(R.drawable.grey_circle);
                break;
        }
    }
}