package com.tokopedia.home.explore.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.home.R;
import com.tokopedia.home.explore.domain.model.ShopData;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.adapter.viewmodel.MyShopViewModel;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class MyShopViewHolder extends AbstractViewHolder<MyShopViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_my_shop;

    TextView titleTxt;
    ImageView imageView;
    TextView button;
    ImageView reputationMedal;
    ImageView badgeImage;
    TextView badgeTxt;

    private CategoryAdapterListener listener;

    public MyShopViewHolder(View itemView, final CategoryAdapterListener listener) {
        super(itemView);
        this.listener = listener;
        titleTxt = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.image_shop);
        badgeImage = itemView.findViewById(R.id.badge);
        button = itemView.findViewById(R.id.btn_ubah);
        reputationMedal = itemView.findViewById(R.id.reputation_medal);
        badgeTxt = itemView.findViewById(R.id.official_store_txt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openShopSetting();
            }
        });
    }

    @Override
    public void bind(MyShopViewModel element) {
        ShopData data = element.getShopData();
        titleTxt.setText(MethodChecker.fromHtml(data.getShopName()));
        ImageHandler.LoadImage(imageView, data.getLogo());
        if (data.getIsOfficial() == 1) {
            badgeTxt.setText(getString(R.string.official_store));
            badgeImage.setImageResource(R.drawable.ic_badge_official);
        } else if (data.isIsGoldBadge()) {
            badgeImage.setImageResource(R.drawable.ic_gold);
            badgeTxt.setText(getString(R.string.gold_merchant));
        } else {
            badgeTxt.setVisibility(View.GONE);
            badgeImage.setVisibility(View.GONE);
        }
        Glide.with(reputationMedal.getContext()).load(data.getReputationBadge())
                .asGif()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(reputationMedal);
    }

}
