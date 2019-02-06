package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.base.EmptyStateListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.widget.TopAdsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by henrypriyono on 10/31/17.
 */

public class EmptySearchViewHolder extends AbstractViewHolder<EmptySearchModel> implements TopAdsItemClickListener {

    public static final String SEARCH_NF_VALUE = "1";
    private final int MAX_TOPADS = 4;
    private TopAdsView topAdsView;
    private TopAdsParams params;
    private Context context;
    private ImageView noResultImage;
    private TextView emptyTitleTextView;
    private TextView emptyContentTextView;
    private Button emptyButtonItemButton;
    private final EmptyStateListener emptyStateListener;
    private TopAdsBannerView topAdsBannerView;
    private RecyclerView selectedFilterRecyclerView;
    private SelectedFilterAdapter selectedFilterAdapter;
    @LayoutRes
    public static final int LAYOUT = R.layout.list_empty_search_product;

    public EmptySearchViewHolder(View view, EmptyStateListener emptyStateListener, Config topAdsConfig) {
        super(view);
        noResultImage = (ImageView) view.findViewById(R.id.no_result_image);
        emptyTitleTextView = (TextView) view.findViewById(R.id.text_view_empty_title_text);
        emptyContentTextView = (TextView) view.findViewById(R.id.text_view_empty_content_text);
        emptyButtonItemButton = (Button) view.findViewById(R.id.button_add_promo);
        this.emptyStateListener = emptyStateListener;
        context = itemView.getContext();
        topAdsView = (TopAdsView) itemView.findViewById(R.id.topads);
        topAdsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.banner_ads);
        selectedFilterRecyclerView = itemView.findViewById(R.id.selectedFilterRecyclerView);

        if (topAdsConfig != null) {
            params = topAdsConfig.getTopAdsParams();
            params.getParam().put(TopAdsParams.KEY_SEARCH_NF, SEARCH_NF_VALUE);
        }
        initSelectedFilterRecyclerView();
    }

    private void initSelectedFilterRecyclerView() {
        selectedFilterAdapter = new SelectedFilterAdapter(emptyStateListener);
        selectedFilterRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        selectedFilterRecyclerView.setAdapter(selectedFilterAdapter);
        selectedFilterRecyclerView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                context.getResources().getDimensionPixelSize(R.dimen.dp_8),
                context.getResources().getDimensionPixelSize(R.dimen.dp_16)
        ));
    }

    private void loadProductAds() {
        Config productAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(emptyStateListener.getUserId())
                .withMerlinCategory()
                .topAdsParams(params)
                .setEndpoint(Endpoint.PRODUCT)
                .build();
        topAdsView.setConfig(productAdsConfig);
        topAdsView.setDisplayMode(DisplayMode.FEED);
        topAdsView.setMaxItems(MAX_TOPADS);
        topAdsView.setAdsItemClickListener(this);
        topAdsView.loadTopAds();
    }

    private void loadBannerAds() {
        Config bannerAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(emptyStateListener.getUserId())
                .withMerlinCategory()
                .topAdsParams(params)
                .setEndpoint(Endpoint.CPM)
                .build();
        topAdsBannerView.setConfig(bannerAdsConfig);
        topAdsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(int position, String appLink, CpmData data) {
                emptyStateListener.onBannerAdsClicked(appLink);
            }
        });
        topAdsBannerView.setAdsListener(new TopAdsListener() {
            @Override
            public void onTopAdsLoaded(List<Item> list) {
                loadProductAds();
            }

            @Override
            public void onTopAdsFailToLoad(int errorCode, String message) {
                loadProductAds();
            }
        });
        topAdsBannerView.loadTopAds();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new com.tokopedia.core.var.ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_ecs());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context);
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        //Not implemented just leave empty
    }

    @Override
    public void onAddFavorite(int position, Data data) {
        //Not implemented just leave empty
    }

    @Override
    public void bind(EmptySearchModel model) {

        noResultImage.setImageResource(model.getImageRes());
        emptyTitleTextView.setText(model.getTitle());

        if (!TextUtils.isEmpty(model.getContent())) {
            emptyContentTextView.setText(boldTextBetweenQuotes(model.getContent()));
            emptyContentTextView.setVisibility(View.VISIBLE);
        } else {
            emptyContentTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(model.getButtonText())) {
            emptyButtonItemButton.setVisibility(View.GONE);
        } else {
            emptyButtonItemButton.setText(model.getButtonText());
            emptyButtonItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (emptyStateListener != null) {
                        emptyStateListener.onEmptyButtonClicked();
                    }
                }
            });
            emptyButtonItemButton.setVisibility(View.VISIBLE);
        }
        if (model.getFilterFlagSelectedModel() != null) {
            selectedFilterRecyclerView.setVisibility(View.VISIBLE);
            selectedFilterAdapter.setOptionList(convertToOptionList(model.getFilterFlagSelectedModel()));
        } else {
            selectedFilterRecyclerView.setVisibility(View.GONE);
        }
        if (params != null) {
            loadBannerAds();
        }
    }

    private CharSequence boldTextBetweenQuotes(String text) {
        String quoteSymbol = "\"";
        int firstQuotePos = text.indexOf(quoteSymbol);
        int lastQuotePos = text.lastIndexOf(quoteSymbol);

        SpannableStringBuilder str = new SpannableStringBuilder(text);
        str.setSpan(new StyleSpan(Typeface.BOLD), firstQuotePos, lastQuotePos + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    private List<Option> convertToOptionList(FilterFlagSelectedModel filterFlagSelectedModel) {
        List<Option> optionList = new ArrayList<>();
        if (filterFlagSelectedModel.getSavedTextInput() != null) {
            if (!TextUtils.isEmpty(filterFlagSelectedModel.getSavedTextInput().get(Option.KEY_PRICE_MIN))
                || !TextUtils.isEmpty(filterFlagSelectedModel.getSavedTextInput().get(Option.KEY_PRICE_MAX))) {
                optionList.add(generatePriceOption());
            }
        }

        if (!TextUtils.isEmpty(filterFlagSelectedModel.getCategoryId())) {
            optionList.add(generateCategoryOption(filterFlagSelectedModel));
        }

        if (filterFlagSelectedModel.getSavedCheckedState() != null) {
            optionList.addAll(generateCheckedOptionList(filterFlagSelectedModel.getSavedCheckedState()));
        }

        return optionList;
    }

    private List<Option> generateCheckedOptionList(HashMap<String, Boolean> savedCheckedState) {
        List<Option> optionList = new ArrayList<>();
        for (HashMap.Entry<String, Boolean> entry : savedCheckedState.entrySet()) {
            optionList.add(generateOptionFromUniqueId(entry.getKey()));
        }
        return optionList;
    }

    private Option generateOptionFromUniqueId(String uniqueId) {
        Option option = new Option();
        option.setName(OptionHelper.parseNameFromUniqueId(uniqueId));
        option.setKey(OptionHelper.parseKeyFromUniqueId(uniqueId));
        option.setValue(OptionHelper.parseValueFromUniqueId(uniqueId));
        return option;
    }

    private Option generateCategoryOption(FilterFlagSelectedModel filterFlagSelectedModel) {
        Option option = new Option();
        option.setName(filterFlagSelectedModel.getSelectedCategoryName());
        option.setKey(Option.KEY_CATEGORY);
        option.setValue(filterFlagSelectedModel.getCategoryId());
        return option;
    }

    private Option generatePriceOption() {
        Option option = new Option();
        option.setName(context.getResources().getString(R.string.empty_state_selected_filter_price_name));
        option.setKey(Option.KEY_PRICE_MIN);
        option.setValue("0");
        return option;
    }

    private static class SelectedFilterAdapter extends RecyclerView.Adapter<SelectedFilterItemViewHolder> {

        private List<Option> optionList = new ArrayList<>();
        private EmptyStateListener clickListener;

        public SelectedFilterAdapter(EmptyStateListener clickListener) {
            this.clickListener = clickListener;
        }

        public void setOptionList(List<Option> optionList) {
            this.optionList.clear();
            this.optionList.addAll(optionList);
            notifyDataSetChanged();
        }

        @Override
        public SelectedFilterItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_state_selected_filter_item, parent, false);
            return new SelectedFilterItemViewHolder(view, clickListener);
        }

        @Override
        public void onBindViewHolder(SelectedFilterItemViewHolder holder, int position) {
            holder.bind(optionList.get(position));
        }

        @Override
        public int getItemCount() {
            return optionList.size();
        }
    }

    private static class SelectedFilterItemViewHolder extends RecyclerView.ViewHolder {
        private TextView filterText;
        private final EmptyStateListener clickListener;
        private View deleteButton;

        public SelectedFilterItemViewHolder(View itemView, EmptyStateListener clickListener) {
            super(itemView);
            filterText = itemView.findViewById(R.id.filter_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
            this.clickListener = clickListener;
        }

        public void bind(final Option option) {
            filterText.setText(option.getName());
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onSelectedFilterRemoved(option.getUniqueId());
                }
            });
        }
    }
}
