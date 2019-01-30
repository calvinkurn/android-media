package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.listener.ShopListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.gm.resource.GMConstant;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class GridShopItemViewHolder extends AbstractViewHolder<ShopViewModel.ShopItem> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_item_shop;

    private LinearLayout mainContent;
    private SquareImageView itemShopImage;
    private ImageView itemShopBadge;
    private TextView itemShopName;
    private ImageView reputationView;
    private TextView shopLocation;
    private ImageView itemPreview1;
    private ImageView itemPreview2;
    private ImageView itemPreview3;
    private View favoriteButton;
    private TextView favoriteButtonText;
    private ImageView favoriteButtonIcon;
    private Context context;
    private final ShopListener itemClickListener;

    public GridShopItemViewHolder(View itemView, ShopListener itemClickListener) {
        super(itemView);
        mainContent = (LinearLayout) itemView.findViewById(R.id.shop_1);
        itemShopImage = (SquareImageView) itemView.findViewById(R.id.item_shop_image);
        itemShopBadge = (ImageView) itemView.findViewById(R.id.item_shop_gold);
        itemShopName = (TextView) itemView.findViewById(R.id.item_shop_name);
        reputationView = (ImageView) itemView.findViewById(R.id.reputation_view);
        shopLocation = (TextView) itemView.findViewById(R.id.shop_location);
        itemPreview1 = (ImageView) itemView.findViewById(R.id.shop_item_preview_1);
        itemPreview2 = (ImageView) itemView.findViewById(R.id.shop_item_preview_2);
        itemPreview3 = (ImageView) itemView.findViewById(R.id.shop_item_preview_3);
        favoriteButton = itemView.findViewById(R.id.shop_list_favorite_button);
        favoriteButtonText = (TextView) itemView.findViewById(R.id.shop_list_favorite_button_text);
        favoriteButtonIcon = (ImageView) itemView.findViewById(R.id.shop_list_favorite_button_icon);
        context = itemView.getContext();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bind(final ShopViewModel.ShopItem shopItem) {
        ImageHandler.loadImageThumbs(context, itemShopImage, shopItem.getShopImage());
        itemShopName.setText(shopItem.getShopName());
        if(shopItem.isOfficial() || shopItem.getShopGoldShop().equals("1")){
            itemShopBadge.setVisibility(View.VISIBLE);
            if(shopItem.isOfficial()) {
                itemShopBadge.setImageResource(com.tokopedia.core2.R.drawable.ic_badge_official);
            } else if(shopItem.getShopGoldShop().equals("1")){
                itemShopBadge.setImageDrawable(GMConstant.getGMDrawable(context));
            }
        } else {
            itemShopBadge.setVisibility(View.GONE);
        }
        mainContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(shopItem, getAdapterPosition());
            }
        });
        shopLocation.setText(shopItem.getShopLocation());
        ImageHandler.LoadImage(reputationView, shopItem.getReputationImageUri());

        try{
            itemPreview1.setVisibility(View.VISIBLE);
            ImageHandler.loadImageAndCache(itemPreview1, shopItem.getProductImages().get(0));
        } catch (NullPointerException|IndexOutOfBoundsException e) {
            itemPreview1.setVisibility(View.INVISIBLE);
        }

        try{
            itemPreview2.setVisibility(View.VISIBLE);
            ImageHandler.loadImageAndCache(itemPreview2, shopItem.getProductImages().get(1));
        } catch (NullPointerException|IndexOutOfBoundsException e) {
            itemPreview2.setVisibility(View.INVISIBLE);
        }

        try{
            ImageHandler.loadImageAndCache(itemPreview3, shopItem.getProductImages().get(2));
        } catch (NullPointerException|IndexOutOfBoundsException e) {
            itemPreview3.setVisibility(View.INVISIBLE);
        }

        adjustFavoriteButtonAppearance(context, shopItem.isFavorited());
        favoriteButton.setEnabled(shopItem.isFavoriteButtonEnabled());
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopItem.isFavoriteButtonEnabled()) {
                    itemClickListener.onFavoriteButtonClicked(shopItem, getAdapterPosition());
                }
            }
        });
    }

    private void adjustFavoriteButtonAppearance(Context context, boolean isFavorited) {
        if (isFavorited) {
            favoriteButton.setBackgroundResource(com.tokopedia.core2.R.drawable.white_button_rounded);
            favoriteButtonText.setText("Favorit");
            favoriteButtonText.setTextColor(context.getResources().getColor(com.tokopedia.core2.R.color.black_54));
            favoriteButtonIcon.setImageResource(com.tokopedia.core2.R.drawable.shop_list_favorite_check);
        } else {
            favoriteButton.setBackgroundResource(com.tokopedia.core2.R.drawable.green_button_rounded);
            favoriteButtonText.setText("Favoritkan");
            favoriteButtonText.setTextColor(context.getResources().getColor(com.tokopedia.core2.R.color.white));
            favoriteButtonIcon.setImageResource(com.tokopedia.core2.R.drawable.ic_add);
        }
    }
}
