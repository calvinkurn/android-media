package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.view.TopAdsBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class HeaderViewHolder extends AbstractViewHolder<HeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_header_layout;
    public static final String DEFAULT_ITEM_VALUE = "1";
    private LinearLayout suggestionContainer;
    private RecyclerView quickFilterListView;
    private TopAdsBannerView adsBannerView;
    private Context context;
    public static final String KEYWORD = "keyword";
    public static final String ETALASE_NAME = "etalase_name";
    private ItemClickListener clickListener;

    public HeaderViewHolder(View itemView, ItemClickListener clickListener, Config topAdsConfig) {
        super(itemView);
        context = itemView.getContext();
        this.clickListener = clickListener;
        suggestionContainer = (LinearLayout) itemView.findViewById(R.id.suggestion_container);
        adsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.ads_banner);
        quickFilterListView = (RecyclerView) itemView.findViewById(R.id.quickFilterListView);
        initTopAds(topAdsConfig);
    }

    private void initTopAds(Config topAdsConfig) {
        TopAdsParams newParam = new TopAdsParams();
        newParam.getParam().putAll(topAdsConfig.getTopAdsParams().getParam());
        newParam.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);
        newParam.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH);
        Config newConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(context))
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(newParam)
                .build();
        adsBannerView.setConfig(newConfig);
        adsBannerView.loadTopAds();
        adsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(String applink) {
                clickListener.onBannerAdsClicked(applink);
            }
        });
    }

    @Override
    public void bind(final HeaderViewModel element) {

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
        bindQuickFilter(element.getQuickFilterList());
    }

    private void bindQuickFilter(List<Option> quickFilterList) {
        QuickFilterAdapter adapter = new QuickFilterAdapter();
        adapter.setOptionList(quickFilterList);
        quickFilterListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        quickFilterListView.setAdapter(adapter);
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

    private static class QuickFilterAdapter extends RecyclerView.Adapter<QuickFilterItemViewHolder> {

        private List<Option> optionList = new ArrayList<>();

        public void setOptionList(List<Option> optionList) {
            this.optionList.clear();
            this.optionList.addAll(optionList);
            notifyDataSetChanged();
        }

        @Override
        public QuickFilterItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quick_filter_item, parent, false);
            return new QuickFilterItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(QuickFilterItemViewHolder holder, int position) {
            holder.bind(optionList.get(position));
        }

        @Override
        public int getItemCount() {
            return optionList.size();
        }
    }

    private static class QuickFilterItemViewHolder extends RecyclerView.ViewHolder {
        private TextView quickFilterText;

        public QuickFilterItemViewHolder(View itemView) {
            super(itemView);
            quickFilterText = itemView.findViewById(R.id.quick_filter_text);
        }

        public void bind(Option option) {
            quickFilterText.setText(option.getName());
            quickFilterText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
