package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
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

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

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
    private QuickFilterAdapter quickFilterAdapter;
    private RecyclerView guidedSearchRecyclerView;
    private GuidedSearchAdapter guidedSearchAdapter;
    private UserSessionInterface userSession;
    private boolean isAdsBannerLoaded = false;

    public HeaderViewHolder(View itemView, ItemClickListener clickListener, Config topAdsConfig) {
        super(itemView);
        context = itemView.getContext();
        userSession = new UserSession(context);
        this.clickListener = clickListener;
        suggestionContainer = (LinearLayout) itemView.findViewById(R.id.suggestion_container);
        adsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.ads_banner);
        quickFilterListView = (RecyclerView) itemView.findViewById(R.id.quickFilterListView);
        guidedSearchRecyclerView = itemView.findViewById(R.id.guidedSearchRecyclerView);
        guidedSearchAdapter = new GuidedSearchAdapter(clickListener);
        guidedSearchRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        guidedSearchRecyclerView.setAdapter(guidedSearchAdapter);
        guidedSearchRecyclerView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                context.getResources().getDimensionPixelSize(R.dimen.dp_8),
                context.getResources().getDimensionPixelSize(R.dimen.dp_16)
        ));
        //initTopAds(topAdsConfig);
        initQuickFilterRecyclerView();
        adsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(String applink) {
                clickListener.onBannerAdsClicked(applink);
            }
        });
    }

    private void initTopAds(Config topAdsConfig) {
        TopAdsParams newParam = new TopAdsParams();
        newParam.getParam().putAll(topAdsConfig.getTopAdsParams().getParam());
        newParam.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);
        newParam.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH);
        Config newConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(userSession.getUserId())
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(newParam)
                .build();
        adsBannerView.setConfig(newConfig);
        adsBannerView.loadTopAds();
    }

    private void initQuickFilterRecyclerView() {
        quickFilterAdapter = new QuickFilterAdapter(clickListener);
        quickFilterListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        quickFilterListView.setAdapter(quickFilterAdapter);
        quickFilterListView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                context.getResources().getDimensionPixelSize(R.dimen.dp_8),
                context.getResources().getDimensionPixelSize(R.dimen.dp_16)
        ));
    }

    @Override
    public void bind(final HeaderViewModel element) {
        if (!isAdsBannerLoaded) {
            adsBannerView.displayAds(element.getCpmModel());
            isAdsBannerLoaded = true;
        }
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
                            clickListener.onSuggestionClicked(element.getSuggestionModel().getSuggestedQuery());
                        }
                    }
                });
                suggestionText.setVisibility(View.VISIBLE);
            } else {
                suggestionText.setVisibility(View.GONE);
            }
            suggestionContainer.addView(suggestionView);
        }
        if (!TextUtils.isEmpty(element.getSuggestionModel().getFormattedResultCount())) {
            quickFilterAdapter.setFormattedResultCount(String.format(context.getString(R.string.result_count_template_text), element.getSuggestionModel().getFormattedResultCount()));
        } else {
            quickFilterAdapter.setFormattedResultCount("");
        }
        quickFilterAdapter.setOptionList(element.getQuickFilterList());
        if (element.getGuidedSearch() != null
                && element.getGuidedSearch().getItemList() != null
                && !element.getGuidedSearch().getItemList().isEmpty()) {
            guidedSearchRecyclerView.setVisibility(View.VISIBLE);
            guidedSearchAdapter.setItemList(element.getGuidedSearch().getItemList());
        } else {
            guidedSearchRecyclerView.setVisibility(View.GONE);
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

    private static class QuickFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER_PRODUCT_COUNT = 0;
        private static final int TYPE_ITEM_QUICK_FILTER = 1;
        private static final int HEADER_COUNT = 1;

        private List<Option> optionList = new ArrayList<>();
        private ItemClickListener clickListener;
        private String formattedResultCount;

        public QuickFilterAdapter(ItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public void setOptionList(List<Option> optionList) {
            this.optionList.clear();
            this.optionList.addAll(optionList);
            notifyDataSetChanged();
        }

        public void setFormattedResultCount(String formattedResultCount) {
            this.formattedResultCount = formattedResultCount;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM_QUICK_FILTER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quick_filter_item, parent, false);
                return new QuickFilterItemViewHolder(view, clickListener);
            } else if (viewType == TYPE_HEADER_PRODUCT_COUNT) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_count_item, parent, false);
                return new ProductCountViewHolder(view);
            }
            throw new RuntimeException("Unknown view type " + viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ProductCountViewHolder) {
                ((ProductCountViewHolder) holder).bind(formattedResultCount);
            } else if (holder instanceof QuickFilterItemViewHolder) {
                ((QuickFilterItemViewHolder) holder).bind(optionList.get(position - HEADER_COUNT));
            }
        }

        @Override
        public int getItemCount() {
            return optionList.size() + HEADER_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeaderPosition(position)) {
                return TYPE_HEADER_PRODUCT_COUNT;
            } else {
                return TYPE_ITEM_QUICK_FILTER;
            }
        }

        private boolean isHeaderPosition(int position) {
            return position < HEADER_COUNT;
        }
    }

    private static class QuickFilterItemViewHolder extends RecyclerView.ViewHolder {
        private TextView quickFilterText;
        private final ItemClickListener clickListener;

        public QuickFilterItemViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);
            quickFilterText = itemView.findViewById(R.id.quick_filter_text);
            this.clickListener = clickListener;
        }

        public void bind(final Option option) {
            quickFilterText.setText(option.getName());
            if (Boolean.parseBoolean(option.getInputState())) {
                quickFilterText.setBackgroundResource(R.drawable.quick_filter_item_background_selected);
            } else {
                quickFilterText.setBackgroundResource(R.drawable.quick_filter_item_background_neutral);
            }
            quickFilterText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onQuickFilterSelected(option);
                }
            });
        }
    }

    private static class ProductCountViewHolder extends RecyclerView.ViewHolder {
        private TextView resultCountText;

        public ProductCountViewHolder(View itemView) {
            super(itemView);
            resultCountText = itemView.findViewById(R.id.result_count_text_view);
        }

        public void bind(String formattedResultCount) {
            if (!TextUtils.isEmpty(formattedResultCount)) {
                resultCountText.setText(formattedResultCount);
                resultCountText.setVisibility(View.VISIBLE);
            } else {
                resultCountText.setVisibility(View.GONE);
            }
        }
    }

    private static class GuidedSearchAdapter extends RecyclerView.Adapter<GuidedSearchViewHolder> {

        List<GuidedSearchViewModel.Item> itemList = new ArrayList<>();
        ItemClickListener itemClickListener;

        public GuidedSearchAdapter(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setItemList(List<GuidedSearchViewModel.Item> itemList) {
            this.itemList = itemList;
            notifyDataSetChanged();
        }

        @Override
        public GuidedSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guided_search_item_with_background, parent, false);
            return new GuidedSearchViewHolder(view, itemClickListener);
        }

        @Override
        public void onBindViewHolder(GuidedSearchViewHolder holder, int position) {
            holder.bind(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    private static class GuidedSearchViewHolder extends RecyclerView.ViewHolder {

        private static final int[] BACKGROUND = {
                R.drawable.guided_back_1,
                R.drawable.guided_back_2,
                R.drawable.guided_back_3,
                R.drawable.guided_back_4,
                R.drawable.guided_back_5,
        };

        TextView textView;
        ImageView imageView;
        ItemClickListener itemClickListener;

        public GuidedSearchViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.guided_search_text);
            imageView = itemView.findViewById(R.id.guided_search_background);
            this.itemClickListener = itemClickListener;
        }

        public void bind(final GuidedSearchViewModel.Item item) {
            textView.setText(item.getKeyword());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(item.getUrl());
                    String query = uri.getQueryParameter(BrowseApi.Q);
                    SearchTracking.eventClickGuidedSearch(textView.getContext(), item.getPreviousKey(), item.getCurrentPage(), item.getKeyword());
                    itemClickListener.onSearchGuideClicked(query);
                }
            });
            imageView.setImageResource(BACKGROUND[getAdapterPosition()]);
        }
    }
}
