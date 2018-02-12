package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Data;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.ImageProduct;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Shop;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo.PromotedShopAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.LocalAdsClickListener;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.SpacesItemDecoration;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.SpannedGridLayoutManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ShopFeedTopAdsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 3/30/17.
 * Copied to feed by milhamj 1/18/17.
 */

public class ShopFeedViewHolder extends AbstractViewHolder<ShopFeedTopAdsViewModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_shop_feed_plus;
    private static final String TAG = ShopFeedViewHolder.class.getSimpleName();
    private static final int MARGIN_15DP_PIXEL = 40;
    private static final int PADDING_12DP_PIXEL = 30;


    private LocalAdsClickListener itemClickListener;
    private ImageView shopImage;
    private TextView shopTitle;
    private TextView shopSubtitle;
    private TextView favTxt;
    private LinearLayout favBtn;
    private LinearLayout container;
    private LinearLayout shopCard;
    private View header;
    private Data data;
    private Context context;
    RecyclerView recyclerView;
    PromotedShopAdapter adapter;
    private int adapterPosition = 0;

    public ShopFeedViewHolder(View itemView,
                              LocalAdsClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        context = itemView.getContext();
        shopCard = (LinearLayout) itemView.findViewById(R.id.shop_topads_card);
        shopImage = (ImageView) itemView.findViewById(R.id.shop_image);
        shopTitle = (TextView) itemView.findViewById(R.id.shop_title);
        shopSubtitle = (TextView) itemView.findViewById(R.id.shop_subtitle);
        favBtn = (LinearLayout) itemView.findViewById(R.id.fav_btn);
        favTxt = (TextView) itemView.findViewById(R.id.fav_text);
        container = (LinearLayout) itemView.findViewById(R.id.container);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        container.setOnClickListener(this);
        header = itemView.findViewById(R.id.header);
        header.setOnClickListener(this);
        favBtn.setOnClickListener(this);
        SpannedGridLayoutManager manager = new SpannedGridLayoutManager(
                new SpannedGridLayoutManager.GridSpanLookup() {
                    @Override
                    public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                        // Conditions for 2x2 items
                        if (position % 6 == 0 || position % 6 == 4) {
                            return new SpannedGridLayoutManager.SpanInfo(2, 2);
                        } else {
                            return new SpannedGridLayoutManager.SpanInfo(1, 1);
                        }
                    }
                },
                3, // number of columns
                1f // how big is default item
        );

        int spacingInPixels = context.getResources().getDimensionPixelSize(
                R.dimen.padding_item_decoration);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(manager);
        adapter = new PromotedShopAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        onClick(id);
    }

    public void onClick(int id) {
        if (itemClickListener != null) {
            if (id == R.id.fav_btn) {
                itemClickListener.onAddFavorite(adapterPosition, data);
            } else if (id == R.id.container || id == R.id.header) {
                itemClickListener.onShopItemClicked(adapterPosition, data);
            }
        }
    }

    @Override
    public void bind(ShopFeedTopAdsViewModel element) {
        data = element.getData();
        Shop shop = data.getShop();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, MARGIN_15DP_PIXEL);
        shopCard.setLayoutParams(lp);
        header.setPadding(0, PADDING_12DP_PIXEL, 0, 0);

        if (shop != null) {
            ImageHandler.LoadImage(shopImage, shop.getImageShop().getXsEcs());
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
            setFavorite(data.isFavorite());
        }


    }

    private void generateThumbnailImages(List<ImageProduct> imageProducts) {
        adapter.setList(mapImageProducts(imageProducts));
    }

    private ArrayList<ProductFeedViewModel> mapImageProducts(List<ImageProduct> imageProducts) {
        ArrayList<ProductFeedViewModel> productFeedViewModels = new ArrayList<>();
        for (ImageProduct imageProduct : imageProducts) {
            ProductFeedViewModel productFeedViewModel = new ProductFeedViewModel(
                    Integer.valueOf(imageProduct.getProductId()),
                    imageProduct.getProductName(),
                    null,
                    imageProduct.getImageUrl(),
                    null,
                    null,
                    null,
                    null,
                    false,
                    null,
                    0
            );
            productFeedViewModels.add(productFeedViewModel);
        }
        return productFeedViewModels;
    }

    private void setFavorite(boolean isFavorite) {
        String text;
        if (isFavorite) {
            favBtn.setSelected(true);
            text = context.getString(R.string.favorited);
            favTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_favorite, 0, 0, 0);
            favTxt.setTextColor(ContextCompat.getColor(context, R.color.label_color));
        } else {
            favBtn.setSelected(false);
            text = context.getString(R.string.favorite);
            favTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_white_24px, 0, 0, 0);
            favTxt.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        favTxt.setText(text);
    }

    private Spanned spannedBadgeString(Spanned text, int drawable) {
        SpannableString spannableString = new SpannableString("  " + text);
        Drawable image;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image = context.getResources().getDrawable(drawable, null);
        } else {
            image = context.getResources().getDrawable(drawable);
        }
        image.setBounds(0, 0, context.getResources().getDimensionPixelOffset(R.dimen.badge_size),
                context.getResources().getDimensionPixelOffset(R.dimen.badge_size));
        spannableString.setSpan(new ImageSpan(image), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }
}
