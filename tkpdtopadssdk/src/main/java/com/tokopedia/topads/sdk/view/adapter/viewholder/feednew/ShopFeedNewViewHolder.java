package com.tokopedia.topads.sdk.view.adapter.viewholder.feednew;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.RoundedCornerImageView;
import com.tokopedia.topads.sdk.view.adapter.FeedNewShopAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ShopFeedNewViewModel;

import java.util.List;

/**
 * @author by milhamj on 29/03/18.
 */

public class ShopFeedNewViewHolder extends AbstractViewHolder<ShopFeedNewViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_shop_feed_new;

    private static final int SPAN_COUNT = 3;

    private Context context;
    private ImageLoader imageLoader;
    private LocalAdsClickListener itemClickListener;
    private int adapterPosition = 0;
    private RoundedCornerImageView shopImage;
    private TextView shopTitle;
    private TextView shopSubtitle;
    private TextView favoriteButton;
    private FeedNewShopAdapter adapter;

    private Data data;

    public ShopFeedNewViewHolder(View itemView, ImageLoader imageLoader,
                                 LocalAdsClickListener clickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.imageLoader = imageLoader;
        this.itemClickListener = clickListener;
        View header = itemView.findViewById(R.id.header);
        shopImage = itemView.findViewById(R.id.shop_image);
        shopTitle = itemView.findViewById(R.id.shop_title);
        shopSubtitle = itemView.findViewById(R.id.shop_subtitle);
        favoriteButton = itemView.findViewById(R.id.favorite_button);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        adapter = new FeedNewShopAdapter(this);

        RecyclerView recyclerView = itemView.findViewById(R.id.product_list);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        itemView.setOnClickListener(onShopItemClicked());
        header.setOnClickListener(onShopItemClicked());
        favoriteButton.setOnClickListener(onAddFavorite());
    }

    public View.OnClickListener onShopItemClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onShopItemClicked(adapterPosition, data);
            }
        };
    }

    private View.OnClickListener onAddFavorite() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onAddFavorite(adapterPosition, data);
            }
        };
    }

    @Override
    public void bind(ShopFeedNewViewModel element) {
        data = element.getData();
        Shop shop = data.getShop();

        if (shop != null) {
            imageLoader.loadImage(shop.getImageShop().getXsEcs(), shop.getImageShop().getsUrl(),
                    shopImage);
            if (shop.getImageProduct() != null) {
                generateThumbnailImages(shop.getImageProduct());
            }

            Spanned title;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title = Html.fromHtml(shop.getName(), Html.FROM_HTML_MODE_LEGACY);
                shopSubtitle.setText(Html.fromHtml(shop.getTagline(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                title = Html.fromHtml(shop.getName());
                shopSubtitle.setText(Html.fromHtml(shop.getTagline()));
            }

            if (shop.isGoldShopBadge()) {
                shopTitle.setText(spannedBadgeString(title, R.drawable.ic_gold));
            } else if (shop.isShop_is_official()) {
                shopTitle.setText(spannedBadgeString(title, R.drawable.ic_official));
            } else {
                shopTitle.setText(title);
            }
            setFavorite(data.isFavorit());
        }
    }

    private Spanned spannedBadgeString(Spanned text, int drawable) {
        SpannableString spannableString = new SpannableString("  " + text);
        Drawable image;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image = context.getResources().getDrawable(drawable, null);
        } else {
            image = context.getResources().getDrawable(drawable);
        }
        image.setBounds(0, 0, context.getResources().getDimensionPixelOffset(R.dimen.badge_size), context.getResources().getDimensionPixelOffset(R.dimen.badge_size));
        spannableString.setSpan(new ImageSpan(image), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void setFavorite(boolean isFavorite) {
        String text;
        if (isFavorite) {
            favoriteButton.setSelected(true);
            text = context.getString(R.string.favorit);
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_favorite, 0, 0, 0);
            favoriteButton.setTextColor(ContextCompat.getColor(context, R.color.label_color));
        } else {
            favoriteButton.setSelected(false);
            text = context.getString(R.string.favoritkan);
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_white_24px, 0, 0, 0);
            favoriteButton.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        favoriteButton.setText(text);
    }

    private void generateThumbnailImages(List<ImageProduct> imageProducts) {
        adapter.setList(imageProducts);
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }
}
