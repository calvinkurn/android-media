package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.analytics.nishikino.model.Promotion;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.design.banner.BannerView;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.loyalty.view.activity.PromoListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BannerViewHolder extends AbstractViewHolder<BannerViewModel> implements BannerView.OnPromoClickListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoAllClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.home_banner;
    private BannerView bannerView;
    private final HomeCategoryListener listener;
    private final Context context;
    private List<BannerSlidesModel> slidesList;

    public BannerViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        bannerView = itemView.findViewById(R.id.banner);
        bannerView.setOnPromoAllClickListener(this);
        bannerView.setOnPromoClickListener(this);
        bannerView.setOnPromoScrolledListener(this);
    }

    @Override
    public void bind(BannerViewModel element) {
        slidesList = element.getSlides();
        List<String> promoUrls = new ArrayList<>();
        for (BannerSlidesModel slidesModel : slidesList) {
            promoUrls.add(slidesModel.getImageUrl());
        }
        bannerView.setPromoList(promoUrls);
        bannerView.buildView();
    }

    private Promotion getPromotion(int position) {
        BannerSlidesModel model = slidesList.get(position);
        Promotion promotion = new Promotion();
        promotion.setPromotionID(String.valueOf(model.getId()));
        promotion.setPromotionName("/ - p1 - promo");
        promotion.setPromotionAlias(model.getTitle().trim().replaceAll(" ", "_"));
        promotion.setPromotionPosition(position + 1);
        promotion.setRedirectUrl(slidesList.get(position).getRedirectUrl());
        return promotion;
    }

    @Override
    public void onPromoClick(int position) {
        HomePageTracking.eventPromoClick(getPromotion(position));
        listener.onPromoClick(slidesList.get(position));
    }

    @Override
    public void onPromoScrolled(int position) {
        if (listener.isMainViewVisible()) {
            HomePageTracking.eventPromoImpression(getPromotion(position));
        }
    }

    @Override
    public void onPromoAllClick() {
        HomePageTracking.eventClickViewAllPromo();

        boolean remoteConfigEnable;
        FirebaseRemoteConfigImpl remoteConfig = new FirebaseRemoteConfigImpl(context);
        remoteConfigEnable = remoteConfig.getBoolean("mainapp_native_promo_list");

        if (remoteConfigEnable) {
            context.startActivity(PromoListActivity.newInstance(context));
        } else {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra(BannerWebView.EXTRA_TITLE, context.getString(R.string.title_activity_promo));
            intent.putExtra(BannerWebView.EXTRA_URL,
                    TkpdBaseURL.URL_PROMO + TkpdBaseURL.FLAG_APP
            );
            context.startActivity(intent);
        }

    }
}