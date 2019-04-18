package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.analytics.HotlistPageTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomMultipleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedQuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewRounderCornerFilterView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.customview.HotlistPromoView;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHashTagViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistPromo;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.track.TrackApp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistHeaderViewHolder extends AbstractViewHolder<HotlistHeaderViewModel> implements QuickSingleFilterView.ActionListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.recyclerview_hotlist_banner;
    public static final String DEFAULT_ITEM_VALUE = "1";
    public static final String HOTLIST_ADS_SRC = "hotlist";
    public static final String SHOP = "shop";
    private DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
    private final Context context;
    private final HotlistListener mHotlistListener;

    private HasTagAdapter hasTagAdapter;
    private final RecyclerView hastagList;
    private final TextView productCount;
    private final HotlistPromoView hotlistPromoView;
    private final TopAdsBannerView topAdsBannerView;
    private final CustomMultipleFilterView customViewRounderCornerFilterView;
    private final String searchQuery;
    private final String hotlistAlias;
    HashMap<String, String> selectedFilterList = new HashMap<>();
    List<QuickFilterItem> filterItems = new ArrayList<>();

    public HotlistHeaderViewHolder(View parent, HotlistListener mHotlistListener, String searchQuery,
                                   String hotlistAlias) {
        super(parent);
        context = parent.getContext();
        this.mHotlistListener = mHotlistListener;
        this.searchQuery = searchQuery;
        this.hotlistAlias = hotlistAlias;
        this.hotlistPromoView = (HotlistPromoView) parent.findViewById(R.id.view_hotlist_promo);
        this.topAdsBannerView = (TopAdsBannerView) parent.findViewById(R.id.topAdsBannerView);
        this.hastagList = parent.findViewById(R.id.hastag_list);
        this.productCount = parent.findViewById(R.id.product_counter);
        this.customViewRounderCornerFilterView = parent.findViewById(R.id.quickFilterView);
        hasTagAdapter = new HasTagAdapter(context, mHotlistListener);
        hastagList.setNestedScrollingEnabled(false);
        hastagList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        hastagList.setAdapter(hasTagAdapter);
        customViewRounderCornerFilterView.setListener(this);
        initTopAds();
    }

    private void initTopAds() {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, HOTLIST_ADS_SRC);
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, searchQuery);
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, mHotlistListener.getUserId());
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(mHotlistListener.getUserId())
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(adsParams)
                .build();
        this.topAdsBannerView.setConfig(config);
        this.topAdsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(int position, String applink, CpmData data) {
                mHotlistListener.onBannerAdsClicked(applink);
                if (applink.contains(SHOP)) {
                    TopAdsGtmTracker.eventHotlistShopPromoClick(context, searchQuery, hotlistAlias, data, position);
                } else {
                    TopAdsGtmTracker.eventHotlistProductPromoClick(context, searchQuery, hotlistAlias, data, position);
                }
            }
        });
        this.topAdsBannerView.setTopAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionHeadlineAdsItem(int position, CpmData data) {
                TopAdsGtmTracker.eventHotlistPromoView(context, hotlistAlias, data, position);
            }
        });
        if (!searchQuery.isEmpty()) {
            this.topAdsBannerView.loadTopAds();
        }
    }

    @Override
    public void bind(HotlistHeaderViewModel element) {
        hasTagAdapter.setData(element.getHashTags());
        if (element.getTotalData() == 0) {
            productCount.setVisibility(View.GONE);
        } else {
            productCount.setVisibility(View.VISIBLE);
            productCount.setText(String.format(context.getString(R.string.product_count), decimalFormat.format(element.getTotalData())));
        }
        if (element.getHotlistPromo() != null) {
            hotlistPromoView.setVisibility(View.VISIBLE);
            renderPromoView(element.getHotlistTitle(), element.getHotlistPromo());
        } else {
            hotlistPromoView.setVisibility(View.GONE);
        }
        renderQuickFilter(element.getOptionList());
    }

    protected void renderQuickFilter(List<Option> filterList) {
        if (filterList == null || filterList.isEmpty()) {
            return;
        }
        if (filterItems.isEmpty()) {
            for (int i = 0; i < filterList.size(); i++) {
                CustomViewRoundedQuickFilterItem quickFilterItem = new CustomViewRoundedQuickFilterItem();
                quickFilterItem.setName(filterList.get(i).getName());
                quickFilterItem.setType(filterList.get(i).getKey() + "=" + filterList.get(i).getValue());
                filterItems.add(quickFilterItem);
            }
        }
        for (QuickFilterItem quickFilterItem: filterItems) {
            String[] str = quickFilterItem.getType().split("=");
            if (!quickFilterItem.isSelected() && selectedFilterList.containsKey(str[0])) {
                selectedFilterList.remove(str[0]);
            }
        }

        mHotlistListener.setQuickFilterList(filterItems);
        customViewRounderCornerFilterView.renderFilter(filterItems);
    }

    private void renderPromoView(final String hotlistTitle, HotlistPromo hotlistPromo) {
        hotlistPromoView.renderData(hotlistPromo, new HotlistPromoView.CallbackListener() {
            @Override
            public void onTncButtonClick(String titlePromo, String voucherCode) {
                HotlistPageTracking.clickTnCButtonHotlistPromo(hotlistTitle, titlePromo, voucherCode);
            }

            @Override
            public void onCopyButtonClick(String titlePromo, String voucherCode) {
                HotlistPageTracking.clickCopyButtonHotlistPromo(hotlistTitle, titlePromo, voucherCode);
            }
        });
        HotlistPageTracking.eventHotlistPromoImpression(hotlistPromoView.getContext(), hotlistTitle, hotlistPromo.getTitle(), hotlistPromo.getVoucherCode());
    }

    @Override
    public void selectFilter(String typeFilter) {
        String[] str = typeFilter.split("=");
        String eventLabel;
        if (selectedFilterList.containsKey(str[0])) {
            selectedFilterList.remove(str[0]);
            eventLabel = "false";
        } else {
            selectedFilterList.put(str[0], str[1]);
            eventLabel = "true";
        }
        eventLabel = str[0] + "-" + str[1] + "-" + eventLabel;
        mHotlistListener.onQuickFilterSelected(selectedFilterList, eventLabel, str[0]);

    }

    private static class HasTagAdapter extends RecyclerView.Adapter<HasTagAdapter.ItemViewHolder> {

        private final Context context;
        private List<HotlistHashTagViewModel> tags;
        private final HotlistListener mHotlistListener;

        public HasTagAdapter(Context context, HotlistListener mHotlistListener) {
            this.context = context;
            this.mHotlistListener = mHotlistListener;
            this.tags = new ArrayList<>();
        }

        public void setData(List<HotlistHashTagViewModel> data) {
            this.tags = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.hastag_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int pos) {
            holder.title.setText(tags.get(pos).getName());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHotlistListener.onHashTagClicked(tags.get(pos).getName(),
                            tags.get(pos).getURL(), tags.get(pos).getDepartmentID());
                }
            });
        }

        @Override
        public int getItemCount() {
            return tags.size();
        }

        static class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ItemViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
            }
        }
    }

}
