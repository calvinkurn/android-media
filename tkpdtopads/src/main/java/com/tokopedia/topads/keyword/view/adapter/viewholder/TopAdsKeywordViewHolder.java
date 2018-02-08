package com.tokopedia.topads.keyword.view.adapter.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.model.NegativeKeywordAd;

/**
 * Created by normansyahputa on 5/19/17.
 */

public class TopAdsKeywordViewHolder extends BaseViewHolder<KeywordAd> {

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

    @Override
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