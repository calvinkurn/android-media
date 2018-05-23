package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.core.analytics.HotlistPageTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistViewPagerAdapter;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.customview.HotlistPromoView;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHashTagViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistPromo;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistHeaderViewHolder extends AbstractViewHolder<HotlistHeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.recyclerview_hotlist_banner;
    public static final String DEFAULT_ITEM_VALUE = "1";
    public static final String HOTLIST_ADS_SRC = "hotlist";

    private final Context context;
    private final ItemClickListener mItemClickListener;

    private HasTagAdapter hasTagAdapter;
    private final RecyclerView hastagList;
    private final TextView productCount;
    private final HotlistPromoView hotlistPromoView;
    private final TopAdsBannerView topAdsBannerView;
    private final String searchQuery;

    public HotlistHeaderViewHolder(View parent, ItemClickListener mItemClickListener, String searchQuery) {
        super(parent);
        context = parent.getContext();
        this.mItemClickListener = mItemClickListener;
        this.searchQuery = searchQuery;
        this.hotlistPromoView = (HotlistPromoView) parent.findViewById(R.id.view_hotlist_promo);
        this.topAdsBannerView = (TopAdsBannerView) parent.findViewById(R.id.topAdsBannerView);
        this.hastagList = parent.findViewById(R.id.hastag_list);
        this.productCount = parent.findViewById(R.id.product_counter);
        hasTagAdapter = new HasTagAdapter(context, mItemClickListener);
        hastagList.setNestedScrollingEnabled(false);
        hastagList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        hastagList.setAdapter(hasTagAdapter);
        initTopAds();
    }

    private void initTopAds() {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, HOTLIST_ADS_SRC);
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, searchQuery);
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(context))
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(adsParams)
                .build();
        this.topAdsBannerView.setConfig(config);
        this.topAdsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(String applink) {
                mItemClickListener.onBannerAdsClicked(applink);
            }
        });
        if (!searchQuery.isEmpty()) {
            this.topAdsBannerView.loadTopAds();
        }
    }

    @Override
    public void bind(HotlistHeaderViewModel element) {
        hasTagAdapter.setData(element.getHashTags());
        productCount.setText(String.format(context.getString(R.string.product_count), element.getTotalData()));
        if (element.getHotlistPromo() != null) {
            hotlistPromoView.setVisibility(View.VISIBLE);
            renderPromoView(element.getHotlistTitle(), element.getHotlistPromo());
        } else {
            hotlistPromoView.setVisibility(View.GONE);
        }
    }

    private void renderPromoView(final String hotlistTitle, HotlistPromo hotlistPromo) {
        hotlistPromoView.renderData(hotlistPromo, new HotlistPromoView.CallbackListener() {
            @Override
            public void onTncButtonClick(String titlePromo, String voucherCode) {
                TrackingUtils.clickTnCButtonHotlistPromo(hotlistTitle, titlePromo, voucherCode);
            }

            @Override
            public void onCopyButtonClick(String titlePromo, String voucherCode) {
                TrackingUtils.clickCopyButtonHotlistPromo(hotlistTitle, titlePromo, voucherCode);
            }
        });
        HotlistPageTracking.eventHotlistPromoImpression(hotlistTitle, hotlistPromo.getTitle(), hotlistPromo.getVoucherCode());
    }

    private static class HasTagAdapter extends RecyclerView.Adapter<HasTagAdapter.ItemViewHolder> {

        private final Context context;
        private List<HotlistHashTagViewModel> tags;
        private final ItemClickListener mItemClickListener;

        public HasTagAdapter(Context context, ItemClickListener mItemClickListener) {
            this.context = context;
            this.mItemClickListener = mItemClickListener;
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
                    mItemClickListener.onHashTagClicked(tags.get(pos).getName(),
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
