package com.tokopedia.flight.banner.view.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.tokopedia.design.banner.BannerPagerAdapter;
import com.tokopedia.design.banner.BannerView;

import java.util.List;

/**
 * Created by nakama on 29/12/17.
 */

public class FlightBannerPagerAdapter extends BannerPagerAdapter {

    private static final int BANNER_WIDTH = 800;
    private final List<String> bannerImageUrls;

    public FlightBannerPagerAdapter(List<String> bannerImageUrls, BannerView.OnPromoClickListener onPromoClickListener) {
        super(bannerImageUrls, onPromoClickListener);
        this.bannerImageUrls = bannerImageUrls;
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        try {
            Glide.with(holder.itemView.getContext())
                    .load(bannerImageUrls.get(position))
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(com.tokopedia.design.R.drawable.ic_loading_image)
                    .error(com.tokopedia.design.R.drawable.ic_loading_image)
                    .override(BANNER_WIDTH, Target.SIZE_ORIGINAL)
                    .into(holder.getBannerImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
