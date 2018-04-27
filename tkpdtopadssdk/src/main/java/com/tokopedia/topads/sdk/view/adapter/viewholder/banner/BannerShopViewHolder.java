package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.TopAdsBannerView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;

import java.util.List;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopViewHolder extends AbstractViewHolder<BannerShopViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_banner_shop;
    private static final String TAG = BannerShopViewHolder.class.getSimpleName();
    private Context context;
    private ImageView iconImg;
    private TextView promotedTxt;
    private TextView nameTxt;
    private TextView descriptionTxt;
    private LinearLayout badgeContainer;
    private final TopAdsBannerClickListener topAdsBannerClickListener;

    public BannerShopViewHolder(View itemView, final TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        context = itemView.getContext();
        iconImg = (ImageView) itemView.findViewById(R.id.icon);
        promotedTxt = (TextView) itemView.findViewById(R.id.title_promote);
        nameTxt = (TextView) itemView.findViewById(R.id.shop_name);
        descriptionTxt = (TextView) itemView.findViewById(R.id.description);
        badgeContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
    }

    @Override
    public void bind(final BannerShopViewModel element) {
        final CpmData.Cpm cpm = element.getCpm();
        if(cpm!=null) {
            Glide.with(context).load(cpm.getCpmImage().getFullEcs()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iconImg.setImageBitmap(resource);
                    new ImpresionTask().execute(cpm.getCpmImage().getFullUrl());
                }
            });
            promotedTxt.setText(cpm.getPromotedText());
            nameTxt.setText(TopAdsBannerView.escapeHTML(cpm.getName()));

            String desc = String.format("%s %s", TopAdsBannerView.escapeHTML(cpm.getDecription()), cpm.getCta());
            TopAdsBannerView.setTextColor(descriptionTxt, desc, cpm.getCta(), ContextCompat.getColor(context, R.color.tkpd_main_green));

            if (cpm.getBadges().size() > 0) {
                badgeContainer.removeAllViews();
                badgeContainer.setVisibility(View.VISIBLE);
                for (Badge badge : cpm.getBadges()) {
                    ImageView badgeImg = new ImageView(context);
                    badgeImg.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.badge_size),
                            context.getResources().getDimensionPixelSize(R.dimen.badge_size)));
                    Glide.with(context).load(badge.getImageUrl()).into(badgeImg);
                    badgeContainer.addView(badgeImg);
                }
            } else {
                badgeContainer.setVisibility(View.GONE);
            }
        }
    }
}
