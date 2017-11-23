package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class HeaderViewHolder extends AbstractViewHolder<HeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_header_layout;
    private LinearLayout osBannerContainer;
    private LinearLayout suggestionContainer;
    private Context context;
    public static final String KEYWORD = "keyword";
    public static final String ETALASE_NAME = "etalase_name";
    private ItemClickListener clickListener;

    public HeaderViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        context = itemView.getContext();
        this.clickListener = clickListener;
        osBannerContainer = (LinearLayout) itemView.findViewById(R.id.official_store_banner_container);
        suggestionContainer = (LinearLayout) itemView.findViewById(R.id.suggestion_container);
    }

    @Override
    public void bind(final HeaderViewModel element) {
        if (element.getOfficialStoreBannerModel() != null &&
                !element.getOfficialStoreBannerModel().getBannerUrl().isEmpty()) {
            UnifyTracking.eventImpressionOsBanner(element.getOfficialStoreBannerModel().getBannerUrl()
                    + " - " + element.getOfficialStoreBannerModel().getKeyword());
            Glide.with(context).load(element.getOfficialStoreBannerModel().getBannerUrl())
                    .asBitmap()
                    .fitCenter()
                    .dontAnimate()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (resource.getHeight() != 1 && resource.getWidth() != 1) {
                                ImageView imageView = new ImageView(context);
                                imageView.setAdjustViewBounds(true);
                                imageView.setImageBitmap(resource);
                                osBannerContainer.removeAllViews();
                                osBannerContainer.addView(imageView);
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        goToUrl(element.getOfficialStoreBannerModel().getShopUrl());

                                        // GTM Tracker
                                        UnifyTracking.eventClickOsBanner(
                                                element.getOfficialStoreBannerModel().getBannerUrl()
                                                        + " - "
                                                        + element.getOfficialStoreBannerModel().getKeyword()
                                        );
                                    }
                                });
                            }
                        }
                    });
        }

        if (element.getSuggestionModel() != null) {
            suggestionContainer.removeAllViews();
            View suggestionView = LayoutInflater.from(context).inflate(R.layout.suggestion_layout, null);
            TextView suggestionText = (TextView) suggestionView.findViewById(R.id.suggestion_text_view);
            TextView resultCountText = (TextView) suggestionView.findViewById(R.id.result_count_text_view);
            if (!TextUtils.isEmpty(element.getSuggestionModel().getSuggestionText())) {
                suggestionText.setText(Html.fromHtml(element.getSuggestionModel().getSuggestionText()));
                suggestionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onSuggestionClicked(element.getSuggestionModel().getSuggestedQuery());
                    }
                });
                suggestionText.setVisibility(View.VISIBLE);
            } else {
                suggestionText.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(element.getSuggestionModel().getFormattedResultCount())) {
                resultCountText.setText(String.format(context.getString(R.string.result_count_template_text), element.getSuggestionModel().getFormattedResultCount()));
                resultCountText.setVisibility(View.VISIBLE);
            } else {
                resultCountText.setVisibility(View.GONE);
            }
            suggestionContainer.addView(suggestionView);
        }

    }

    private void goToUrl(String url) {
        switch ((DeepLinkChecker.getDeepLinkType(url))) {
            case DeepLinkChecker.BROWSE:
                DeepLinkChecker.openBrowse(url, context);
                break;
            case DeepLinkChecker.HOT:
                DeepLinkChecker.openHot(url, context);
                break;
            case DeepLinkChecker.HOT_LIST:
                DeepLinkChecker.openHomepage(context, HomeRouter.INIT_STATE_FRAGMENT_HOTLIST);
                break;
            case DeepLinkChecker.CATALOG:
                DeepLinkChecker.openCatalog(url, context);
                break;
            case DeepLinkChecker.PRODUCT:
                DeepLinkChecker.openProduct(url, context);
                break;
            case DeepLinkChecker.SHOP:
                Bundle bundle = new Bundle();
                if (DeepLinkChecker.getQuery(url, KEYWORD) != null) {
                    bundle.putString(KEYWORD, DeepLinkChecker.getQuery(url, KEYWORD));
                }
                DeepLinkChecker.openShopWithParameter(url, context, bundle);
                break;
            case DeepLinkChecker.ETALASE:
                bundle = new Bundle();
                bundle.putString(ETALASE_NAME, DeepLinkChecker.getLinkSegment(url).get(2));
                if (DeepLinkChecker.getQuery(url, KEYWORD) != null) {
                    bundle.putString(KEYWORD, DeepLinkChecker.getQuery(url, KEYWORD));
                }
                DeepLinkChecker.openShopWithParameter(url, context, bundle);
                break;
            default:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
                break;
        }
    }

}
