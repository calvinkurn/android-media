package com.tokopedia.topads.sdk.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.ShopImageListAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopListViewModel;

/**
 * @author by errysuprayogi on 3/30/17.
 */

public class ShopListViewHolder extends AbstractViewHolder<ShopListViewModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_shop_list;
    private static final String TAG = ShopListViewHolder.class.getSimpleName();


    private LocalAdsClickListener itemClickListener;
    private ImageView shopImage;
    private TextView shopTitle;
    private TextView shopSubtitle;
    private RecyclerView shopListImage;
    private LinearLayout favBtn;
    private Data data;
    private Context context;
    private SnapHelper snapHelper;
    private ImageLoader imageLoader;

    public ShopListViewHolder(View itemView, ImageLoader imageLoader, LocalAdsClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        this.imageLoader = imageLoader;
        context = itemView.getContext();
        shopImage = (ImageView) itemView.findViewById(R.id.shop_image);
        shopTitle = (TextView) itemView.findViewById(R.id.shop_title);
        shopSubtitle = (TextView) itemView.findViewById(R.id.shop_subtitle);
        shopListImage = (RecyclerView) itemView.findViewById(R.id.image_list);
        favBtn = (LinearLayout) itemView.findViewById(R.id.fav_btn);
        itemView.setOnClickListener(this);
        favBtn.setOnClickListener(this);
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(shopListImage);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(itemClickListener!=null) {
            if (id == R.id.fav_btn) {
                itemClickListener.onAddFavorite(getAdapterPosition(), data.getShop());
            } else {
                itemClickListener.onShopItemClicked(getAdapterPosition(), data);
            }
        }
    }

    @Override
    public void bind(ShopListViewModel element) {
        data = element.getData();
        Shop shop = data.getShop();
        if(shop!=null){
            imageLoader.loadImage(shop.getImageShop().getXsEcs(), shop.getImageShop().getXsUrl(),
                    shopImage);
            if(shop.getImageProduct()!=null){
                ShopImageListAdapter imageListAdapter = new ShopImageListAdapter(context, imageLoader, shop.getImageProduct(), this);
                shopListImage.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                shopListImage.setHasFixedSize(true);
                shopListImage.setAdapter(imageListAdapter);
            }

            SpannableString spannableString;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                spannableString = new SpannableString("  "+Html.fromHtml(shop.getTagline(), Html.FROM_HTML_MODE_LEGACY));
                shopSubtitle.setText(Html.fromHtml(shop.getTagline(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                spannableString = new SpannableString("  "+Html.fromHtml(shop.getTagline()));
                shopSubtitle.setText(Html.fromHtml(shop.getTagline()));
            }

            if(shop.isGoldShopBadge()){
                Drawable image = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image = context.getResources().getDrawable(R.drawable.ic_gold, null);
                } else {
                    image = context.getResources().getDrawable(R.drawable.ic_gold);
                }
                image.setBounds(0, 0, context.getResources().getDimensionPixelOffset(R.dimen.badge_size),
                        context.getResources().getDimensionPixelOffset(R.dimen.badge_size));
                spannableString.setSpan(new ImageSpan(image), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(shop.isShop_is_official()) {
                Drawable image = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image = context.getResources().getDrawable(R.drawable.ic_official, null);
                } else {
                    image = context.getResources().getDrawable(R.drawable.ic_official);
                }
                image.setBounds(0, 0, context.getResources().getDimensionPixelOffset(R.dimen.badge_size),
                        context.getResources().getDimensionPixelOffset(R.dimen.badge_size));
                spannableString.setSpan(new ImageSpan(image), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            shopTitle.setText(spannableString);
        }
    }
}
