package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class HeaderViewHolder extends AbstractViewHolder<HeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_header_layout;
    public static final String SHOP = "shop";
    private LinearLayout suggestionContainer;
    private TopAdsBannerView adsBannerView;
    private Context context;
    private ProductListener productListener;

    public HeaderViewHolder(View itemView, ProductListener productListener, String searchQuery) {
        super(itemView);
        context = itemView.getContext();
        this.productListener = productListener;
        suggestionContainer = (LinearLayout) itemView.findViewById(R.id.suggestion_container);
        adsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.ads_banner);
        adsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(int position, String applink, CpmData data) {
                productListener.onBannerAdsClicked(applink);
                if (applink.contains(SHOP)) {
                    TopAdsGtmTracker.eventSearchResultPromoShopClick(context, data, position);
                } else {
                    TopAdsGtmTracker.eventSearchResultPromoProductClick(context, data, position);
                }
            }
        });
        adsBannerView.setTopAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionHeadlineAdsItem(int position, CpmData data) {
                TopAdsGtmTracker.eventSearchResultPromoView(context, data, position);
            }
        });
    }

    @Override
    public void bind(final HeaderViewModel element) {
        adsBannerView.displayAds(element.getCpmModel());
        if (element.getSuggestionModel() != null) {
            suggestionContainer.removeAllViews();
            View suggestionView = LayoutInflater.from(context).inflate(R.layout.suggestion_layout, null);
            TextView suggestionText = (TextView) suggestionView.findViewById(R.id.suggestion_text_view);
            if (!TextUtils.isEmpty(element.getSuggestionModel().getSuggestionText())) {
                suggestionText.setText(Html.fromHtml(element.getSuggestionModel().getSuggestionText()));
                suggestionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(element.getSuggestionModel().getSuggestedQuery())) {
                            productListener.onSuggestionClicked(element.getSuggestionModel().getSuggestedQuery());
                        }
                    }
                });
                suggestionText.setVisibility(View.VISIBLE);
            } else {
                suggestionText.setVisibility(View.GONE);
            }
            suggestionContainer.addView(suggestionView);
        }
    }
}
