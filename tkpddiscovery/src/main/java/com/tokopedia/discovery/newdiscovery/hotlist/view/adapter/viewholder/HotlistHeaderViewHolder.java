package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
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

    private final CirclePageIndicator indicator;
    private final ViewPager viewpager;
    private final LinearLayout containerHashtag;
    private final RelativeLayout hotlistBackground;
    private final View hashtTagScrollView;
    private final HotlistPromoView hotlistPromoView;
    private final TopAdsBannerView topAdsBannerView;
    private int counterError;
    private final String searchQuery;

    public HotlistHeaderViewHolder(View parent, ItemClickListener mItemClickListener, String searchQuery) {
        super(parent);
        context = parent.getContext();
        this.mItemClickListener = mItemClickListener;
        this.searchQuery = searchQuery;
        this.indicator = (CirclePageIndicator) parent.findViewById(R.id.hot_list_banner_indicator);
        this.viewpager = (ViewPager) parent.findViewById(R.id.hot_list_banner_view_pager);
        this.containerHashtag = (LinearLayout) parent.findViewById(R.id.hot_list_banner_hashtags);
        this.hotlistBackground = (RelativeLayout) parent.findViewById(R.id.hotlist_background);
        this.hashtTagScrollView = parent.findViewById(R.id.hashtag_scroll_view);
        this.hotlistPromoView = (HotlistPromoView) parent.findViewById(R.id.view_hotlist_promo);
        this.topAdsBannerView = (TopAdsBannerView) parent.findViewById(R.id.topAdsBannerView);
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
        HotlistViewPagerAdapter adapter = new HotlistViewPagerAdapter(context, element.getDesc(), mItemClickListener);
        renderImage(element.getImageUrl());
        viewpager.setAdapter(adapter);
        indicator.setViewPager(viewpager);
        indicator.setFillColor(ContextCompat.getColor(context, R.color.tkpd_dark_green));
        indicator.setStrokeColor(ContextCompat.getColor(context, R.color.white));
        if (element.getHashTags() != null && !element.getHashTags().isEmpty()) {
            hashtTagScrollView.setVisibility(View.VISIBLE);
            renderHashtag(element.getHashTags());
        } else {
            hashtTagScrollView.setVisibility(View.GONE);
        }

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
    }

    private void renderHashtag(final List<HotlistHashTagViewModel> hashTags) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        containerHashtag.removeAllViews();
        for (int i = 0; i < hashTags.size(); i++) {
            final int pos = i;
            View view = vi.inflate(R.layout.layout_textview_hashtag, null);
            TextView texthash = (TextView) view.findViewById(R.id.hashtags_txt);
            texthash.setText(String.format("#%s", hashTags.get(pos).getName()));
            texthash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onHashTagClicked(hashTags.get(pos).getName(),
                            hashTags.get(pos).getURL(), hashTags.get(pos).getDepartmentID());
                }
            });
            containerHashtag.addView(view);
        }
    }

    private void renderImage(final String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .centerCrop()
                .dontAnimate()
                .placeholder(com.tokopedia.core.R.drawable.loading_page)
                .error(com.tokopedia.core.R.drawable.error_drawable)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        counterError++;
                        if (counterError < 5) {
                            ImageHandler.loadImageBitmap2(itemView.getContext(), imageUrl, this);
                        }
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        counterError = 0;
                        Drawable drawable;
                        drawable = new BitmapDrawable(itemView.getResources(), resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            hotlistBackground.setBackground(drawable);
                        } else {
                            hotlistBackground.setBackgroundDrawable(drawable);
                        }
                    }


                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        counterError = 0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            hotlistBackground.setBackground(placeholder);
                        } else {
                            hotlistBackground.setBackgroundDrawable(placeholder);
                        }
                    }
                });
    }
}
