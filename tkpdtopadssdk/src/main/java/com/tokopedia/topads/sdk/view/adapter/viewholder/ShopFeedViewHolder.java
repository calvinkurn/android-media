package com.tokopedia.topads.sdk.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.SpacesItemDecoration;
import com.tokopedia.topads.sdk.view.SquareImageView;
import com.tokopedia.topads.sdk.view.adapter.PromotedShopAdapter;
import com.tokopedia.topads.sdk.view.adapter.ShopImageListAdapter;
import com.tokopedia.topads.sdk.view.adapter.SpannedGridLayoutManager;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopFeedViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopListViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 3/30/17.
 */

public class ShopFeedViewHolder extends AbstractViewHolder<ShopFeedViewModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_shop_feed_plus;
    private static final String TAG = ShopFeedViewHolder.class.getSimpleName();


    private LocalAdsClickListener itemClickListener;
    private ImageView shopImage;
    private TextView shopTitle;
    private TextView shopSubtitle;
    private TextView favTxt;
    private LinearLayout favBtn;
    private LinearLayout container;
    private View header;
    private Data data;
    private Context context;
    private ImageLoader imageLoader;
    RecyclerView recyclerView;
    PromotedShopAdapter adapter;
    private int adapterPosition = 0;

    public ShopFeedViewHolder(View itemView, ImageLoader imageLoader,
                              LocalAdsClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        this.imageLoader = imageLoader;
        context = itemView.getContext();
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

        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.padding_item_decoration);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(manager);
        adapter = new PromotedShopAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        onClick(id);
    }

    public void onClick(int id) {
        if(itemClickListener!=null) {
            if (id == R.id.fav_btn) {
                itemClickListener.onAddFavorite(adapterPosition, data);
            } else if (id == R.id.container || id == R.id.header){
                itemClickListener.onShopItemClicked(adapterPosition, data);
            }
        }
    }

    @Override
    public void bind(ShopFeedViewModel element) {
        data = element.getData();
        Shop shop = data.getShop();
        if(shop!=null){
            imageLoader.loadImage(shop.getImageShop().getXsEcs(), shop.getImageShop().getsUrl(),
                    shopImage);
            if(shop.getImageProduct()!=null){
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

            if(shop.isGoldShopBadge()){
                shopTitle.setText(spannedBadgeString(title, R.drawable.ic_gold));
            } else if(shop.isShop_is_official()) {
                shopTitle.setText(spannedBadgeString(title, R.drawable.ic_official));
            } else {
                shopTitle.setText(title);
            }
            setFavorite(data.isFavorit());
        }


    }

    private void generateThumbnailImages(List<ImageProduct> imageProducts) {
        adapter.setList(imageProducts);
    }

    private void setFavorite(boolean isFavorite){
        String text;
        if (isFavorite) {
            favBtn.setSelected(true);
            text = context.getString(R.string.favorited);
            favTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_favorite, 0, 0, 0);
            favTxt.setTextColor(ContextCompat.getColor(context, R.color.label_color));
        } else {
            favBtn.setSelected(false);
            text = context.getString(R.string.favoritkan);
            favTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_black_24dp, 0, 0, 0);
            favTxt.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        favTxt.setText(text);
    }

    private Spanned spannedBadgeString(Spanned text, int drawable){
        SpannableString spannableString = new SpannableString("  "+text);
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
