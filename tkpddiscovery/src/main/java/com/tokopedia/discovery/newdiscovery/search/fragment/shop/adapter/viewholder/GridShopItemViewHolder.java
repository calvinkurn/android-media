package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.PreviewItemAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.decoration.ShopListItemDecoration;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.listener.ShopListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.gm.resource.GMConstant;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class GridShopItemViewHolder extends AbstractViewHolder<ShopViewModel.ShopItem> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_item_shop;

    private static final String KEY_SHOP_IS_GOLD = "1";
    private static final String KEY_SHOP_IS_INACTIVE = "4";
    private static final String KEY_SHOP_IS_CLOSED = "2";

    private LinearLayout mainContent;
    private SquareImageView itemShopImage;
    private ImageView itemShopBadge;
    private TextView itemShopName;
    private ImageView reputationView;
    private TextView shopLocation;
    private View favoriteButton;
    private TextView favoriteButtonText;
    private ImageView favoriteButtonIcon;
    private View viewShopInactive;
    private RecyclerView rvItemPreview;
    private Context context;
    private TextViewCompat tv_unavailable_label;
    private final ShopListener itemClickListener;

    public GridShopItemViewHolder(View itemView, ShopListener itemClickListener) {
        super(itemView);
        mainContent = (LinearLayout) itemView.findViewById(R.id.shop_1);
        itemShopImage = (SquareImageView) itemView.findViewById(R.id.item_shop_image);
        itemShopBadge = (ImageView) itemView.findViewById(R.id.item_shop_gold);
        itemShopName = (TextView) itemView.findViewById(R.id.item_shop_name);
        reputationView = (ImageView) itemView.findViewById(R.id.reputation_view);
        shopLocation = (TextView) itemView.findViewById(R.id.shop_location);
        favoriteButton = itemView.findViewById(R.id.shop_list_favorite_button);
        favoriteButtonText = (TextView) itemView.findViewById(R.id.shop_list_favorite_button_text);
        favoriteButtonIcon = (ImageView) itemView.findViewById(R.id.shop_list_favorite_button_icon);
        viewShopInactive = itemView.findViewById(R.id.view_shop_inactive);
        rvItemPreview = itemView.findViewById(R.id.rv_item_preview);
        context = itemView.getContext();
        tv_unavailable_label = itemView.findViewById(R.id.tv_unavailable_label);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bind(final ShopViewModel.ShopItem shopItem) {
        ImageHandler.loadImageCircle2(context, itemShopImage, shopItem.getShopImage());
        itemShopName.setText(shopItem.getShopName());
        if(shopItem.isOfficial() || shopItem.getShopGoldShop().equals(KEY_SHOP_IS_GOLD)){
            itemShopBadge.setVisibility(View.VISIBLE);
            if(shopItem.isOfficial()) {
                itemShopBadge.setImageResource(R.drawable.ic_official_store_discovery);
            } else if(shopItem.getShopGoldShop().equals(KEY_SHOP_IS_GOLD)){
                itemShopBadge.setImageDrawable(GMConstant.getGMDrawable(context));
            }
        } else {
            itemShopBadge.setVisibility(View.GONE);
        }

        if (shopItem.getShopStatus().equals(KEY_SHOP_IS_INACTIVE)) {
            viewShopInactive.setVisibility(View.VISIBLE);
            tv_unavailable_label.setText(
                    context.getString(R.string.label_shop_inactive)
            );
        } else if (shopItem.getShopStatus().equals(KEY_SHOP_IS_CLOSED)) {
            viewShopInactive.setVisibility(View.VISIBLE);
            tv_unavailable_label.setText(
                    context.getString(R.string.label_shop_close)
            );
        } else {
            viewShopInactive.setVisibility(View.GONE);
        }

        mainContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(shopItem, getAdapterPosition());
            }
        });
        shopLocation.setText(shopItem.getShopLocation());
        ImageHandler.LoadImage(reputationView, shopItem.getReputationImageUri());

        if (shopItem.getProductImages().size() > 0){
            rvItemPreview.setVisibility(View.VISIBLE);
            PreviewItemAdapter previewItemAdapter = new PreviewItemAdapter(
                    context,
                    getPreviewImageSize(context)
            );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    context, LinearLayoutManager.HORIZONTAL, false
            );
            rvItemPreview.setLayoutManager(linearLayoutManager);
            rvItemPreview.setAdapter(previewItemAdapter);
            if (rvItemPreview.getItemDecorationCount() == 0) {
                rvItemPreview.addItemDecoration(getDecoration());
            }
            previewItemAdapter.setData(shopItem.getProductImages());
        } else {
            hideShopPreviewItems(rvItemPreview);
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

    protected RecyclerView.ItemDecoration getDecoration() {
        return new ShopListItemDecoration(
                context.getResources().getDimensionPixelSize(R.dimen.dp_2),
                context.getResources().getDimensionPixelSize(R.dimen.dp_2),
                0,
                0);
    }

    protected int getPreviewImageSize(Context context){
        return (int)context.getResources().getDimension(R.dimen.shop_item_preview_size_grid);
    }

    protected void hideShopPreviewItems(View viewPreviewItems){
        viewPreviewItems.setVisibility(View.VISIBLE);
    }

    private void adjustFavoriteButtonAppearance(Context context, boolean isFavorited) {
        if (isFavorited) {
            favoriteButton.setBackgroundResource(R.drawable.white_button_rounded);
            favoriteButtonText.setText(context.getString(R.string.label_following_shop));
            favoriteButtonText.setTextColor(context.getResources().getColor(com.tokopedia.core2.R.color.black_54));
            favoriteButtonIcon.setImageResource(R.drawable.shop_list_favorite_check);
        } else {
            favoriteButton.setBackgroundResource(R.drawable.green_button_rounded);
            favoriteButtonText.setText(context.getString(R.string.label_follow_shop));
            favoriteButtonText.setTextColor(context.getResources().getColor(com.tokopedia.core2.R.color.white));
            favoriteButtonIcon.setImageResource(R.drawable.ic_add);
        }
    }
}
